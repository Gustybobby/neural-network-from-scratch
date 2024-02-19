"use client"

import MonteCarlo from "@/classes/monte-carlo"
import TicTacToe from "@/classes/tic-tac-toe"
import { useEffect, useState } from "react"

const streakToWin = 3 
const size = 3
const initState = '-'.repeat(size*size)
const initIter = 20000
const roundIter = 5000

export default function Grid(){
    const [ticTacToe, setTicTacToe] = useState(TicTacToe.initialize(initState, 'X', streakToWin))
    const [turn, setTurn] = useState<'X' | 'O'>('X')
    const [monteCarlo, setMonteCarlo] = useState(new MonteCarlo({
        initialStateString: initState,
        player: 'X',
        streakToWin: streakToWin,
        stateValues: {},
        greed: 0.3,
        stepSize: 10000/initIter,
        startFirst: true
    }))
    useEffect(() => {
        if(turn !== 'O' || ticTacToe.gameState !== 'P'){
            setTurn('X')
            return
        }
        monteCarlo.initialStateString = TicTacToe.boardToStrings(ticTacToe.board)
        monteCarlo.iterate(roundIter)
        setMonteCarlo(new MonteCarlo({
            ...monteCarlo,
            player: monteCarlo.ticTacToe.player,
            streakToWin: monteCarlo.ticTacToe.streakToWin,
            startFirst: false
        }))
        ticTacToe.player = TicTacToe.switchPlayer(ticTacToe.player)
        const stateActions = ticTacToe.getAllNextStateActions()
        if(stateActions.length === 0){
            ticTacToe.player = TicTacToe.switchPlayer(ticTacToe.player)
            setTurn('X')
            return
        }
        const opt = monteCarlo.getOptimalNextState(stateActions, false)
        ticTacToe.move(opt.action.row,opt.action.col)
        ticTacToe.player = TicTacToe.switchPlayer(ticTacToe.player)
        setTurn('X')
        setTicTacToe(new TicTacToe({ ...ticTacToe }))
    },[turn, ticTacToe])

    return (
        <div className="flex flex-col border border-black text-black">
            <button
                className="bg-purple-300 p-2 shadow-lg hover:bg-purple-400 transition-colors font-bold text-xl"
                onClick={() => {
                    monteCarlo.iterate(initIter)
                    setMonteCarlo(new MonteCarlo({
                        ...monteCarlo,
                        player: monteCarlo.ticTacToe.player,
                        streakToWin: monteCarlo.ticTacToe.streakToWin,
                    }))
                }}
            >
                Build AI
            </button>
            <button
                className="bg-green-300 p-2 shadow-lg hover:bg-green-400 transition-colors font-bold text-xl"
                onClick={()=>setTicTacToe(TicTacToe.initialize(initState, 'X', streakToWin))}
            >
                Reset
            </button>
            <span className="text-center text-2xl">{ticTacToe.player} {ticTacToe.gameState}</span>
            {ticTacToe.board.map((row, i) => (
            <div key={'ROW'+i} className="flex flex-row">
                {row.map((cell, j) => (
                    <button
                        key={'CELL'+i+'_'+j}
                        className={styles.cell(cell !== '-' || ticTacToe.gameState !== 'P')}
                        disabled={cell !== '-' || ticTacToe.gameState !== 'P'}
                        onClick={() => {
                            setTicTacToe(ticTacToe => {
                                const tTT = new TicTacToe({
                                    ...ticTacToe,
                                    board: TicTacToe.cloneBoard(ticTacToe.board),
                                })
                                tTT.move(i,j)
                                return tTT
                            })
                            setTurn('O')
                        }}
                    >
                        <div className="flex flex-col">
                            <span>{cell}</span>
                        </div>
                    </button>
                ))}
            </div>
            ))}
        </div>
    )
}

const styles = {
    cell: (disabled: boolean) => [
        'w-16 h-16 flex justify-center items-center',
        'font-bold text-black text-3xl',
        'border border-black',
        'transition-colors',
        disabled? 'bg-yellow-100' : 'hover:bg-blue-100',
    ].join(' ')
}