import TicTacToe, { NextStateAction } from "./tic-tac-toe";

interface StateValues {
    [state: string]: {
        value: number
        updates: number
    }
}

interface Episode {
    reward: number
    state: string
}

interface MonteCarloProperty {
    initialStateString: string
    stateValues: StateValues
    greed: number
    stepSize: number
    player: 'X' | 'O'
    streakToWin: number
    startFirst: boolean
}

export default class MonteCarlo {

    initialStateString: string
    ticTacToe: TicTacToe
    stateValues: StateValues
    greed: number
    stepSize: number
    startFirst: boolean

    constructor(monteCarlo: MonteCarloProperty){
        this.initialStateString = monteCarlo.initialStateString
        this.ticTacToe = TicTacToe.initialize(monteCarlo.initialStateString, monteCarlo.player, monteCarlo.streakToWin)
        this.stateValues = monteCarlo.stateValues
        this.greed = monteCarlo.greed
        this.stepSize = monteCarlo.stepSize
        this.startFirst = monteCarlo.startFirst
    }

    simulate(){
        this.ticTacToe.board = TicTacToe.createBoardFromString(this.initialStateString)
        const initPlayer = this.ticTacToe.player
        const initGameState = this.ticTacToe.gameState
        let player = this.startFirst? initPlayer : TicTacToe.switchPlayer(initPlayer)
        const episodes: Episode[] = []
        let stateString = this.getStateData(this.ticTacToe.board).repString
        while(true){
            episodes.push({
                state: stateString,
                reward: (player === 'O'? 1 : -1)*this.ticTacToe.getReward()
            })
            if(this.ticTacToe.gameState !== 'P'){
                break
            }
            const randomizeAction = Math.random() < this.greed
            const nextStateActions = this.ticTacToe.getAllNextStateActions()
            if(nextStateActions.length === 0){
                break
            }
            let action = nextStateActions[0].action
            if(randomizeAction){
                const actionIndex = Math.min(nextStateActions.length-1, Math.round(nextStateActions.length*Math.random()))
                const stateData = this.getStateData(nextStateActions[actionIndex].nextState)
                action = nextStateActions[actionIndex].action
                stateString = stateData.repString
            } else {
                const { action: optimalAction, data } = this.getOptimalNextState(nextStateActions, this.ticTacToe.player === 'X')
                action = optimalAction
                stateString = data.repString
            }
            this.ticTacToe.move(action.row, action.col)
            player = TicTacToe.switchPlayer(player)
            this.ticTacToe.player = player
        }
        this.ticTacToe.player = initPlayer
        this.ticTacToe.board = TicTacToe.createBoardFromString(this.initialStateString)
        this.ticTacToe.gameState = initGameState
        return episodes
    }

    getOptimalNextState(nextStateActions: NextStateAction[], isPlayer: boolean){
        const stateActionDataList = nextStateActions.map(({ nextState,action }) => ({
            data: this.getStateData(nextState),
            action,
        }))
        let { data: maxData, action: maxAction } = stateActionDataList[0]
        for(const { data, action } of stateActionDataList){
            if(isPlayer && data.value > maxData.value || !isPlayer && data.value < maxData.value){
                maxData = data
                maxAction = action
            }
        }
        return { data: maxData, action: maxAction }
    }

    getStateData(state: TicTacToe['board']){
        const eqBoardStrings = TicTacToe.getEquivalentBoards(state)
        let repString: string | undefined = undefined
        for(const eqString of eqBoardStrings){
            if(this.stateValues[eqString] !== undefined){
                repString = eqString
                break
            }
        }
        return {
            repString: repString ?? eqBoardStrings[0],
            value: repString? this.stateValues[repString].value : 0
        }
    }

    updateStateValues(episodes: Episode[]){
        let nextEpStateValue = null
        for(var i=episodes.length-1;i>=0;i--){
            const ep = episodes[i]
            this.stateValues[ep.state] = this.stateValues[ep.state] ?? { value: 0, updates: 0 }
            if(nextEpStateValue === null){
                this.stateValues[ep.state] = { value: ep.reward, updates: 1 }
            } else {
                this.stateValues[ep.state].updates += 1
                const lr = 1/this.stateValues[ep.state].updates
                this.stateValues[ep.state].value += lr*(ep.reward + 0.9*nextEpStateValue.value - this.stateValues[ep.state].value)
            }
            nextEpStateValue = this.stateValues[ep.state]
        }
    }

    iterate(iter: number){
        for(var i=0;i<iter;i++){
            const episodes = this.simulate()
            this.updateStateValues(episodes)
            if((i+1)%1000 === 0){
                console.log(`${i+1}/${iter}`,'simulations')
            }
        }
    }
}