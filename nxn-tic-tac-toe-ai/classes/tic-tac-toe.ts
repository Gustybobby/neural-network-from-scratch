type CellSymbol = 'X' | 'O' | '-'

type GameState = 'P' | 'W' | 'L'

interface TicTacToeProperty {
    player: 'X' | 'O',
    board: CellSymbol[][],
    streakToWin: number
    gameState: GameState
}

export interface NextStateAction {
    nextState: TicTacToeProperty['board']
    action: { row: number, col: number }
}

export default class TicTacToe {

    player: TicTacToeProperty['player']
    board: TicTacToeProperty['board']
    streakToWin: TicTacToeProperty['streakToWin']
    gameState: TicTacToeProperty['gameState']

    static directionList = [{ x: 0, y: 1 }, { x: 1, y: 0 }, { x: 1, y: 1 }, { x: -1, y: 1 }]
    
    constructor(ticTacToe: TicTacToeProperty){
        this.player = ticTacToe.player
        this.board = ticTacToe.board
        this.streakToWin = ticTacToe.streakToWin
        this.gameState = ticTacToe.gameState
    }

    static initialize(
        initialStateString: string,
        player: TicTacToeProperty['player'],
        streakToWin: TicTacToeProperty['streakToWin'],
    ){
        const newBoard = TicTacToe.createBoardFromString(initialStateString)
        return new TicTacToe({
            board: newBoard,
            player,
            streakToWin,
            gameState: TicTacToe.evaluateBoard(newBoard, player, streakToWin)
        })
    }

    static createBoardFromString(initialStateString: string): TicTacToeProperty['board']{
        const width = Math.sqrt(initialStateString.length)
        if(!Number.isInteger(width)){
            throw 'Initial State Length is not a perfect square'
        }
        const board: TicTacToeProperty['board'] = []
        for(var i = 0;i < width;i++){
            const row: CellSymbol[] = []
            for(var j = 0;j < width;j++){
                const char = initialStateString[i*width+j]
                if(char !== 'X' && char !== 'O' && char !== '-'){
                    throw 'string can only contains X, O, or -'
                }
                row.push(char)
            }
            board.push(row)
        }
        return board
    }

    static cloneBoard(board: TicTacToeProperty['board']): TicTacToeProperty['board']{
        return board.map((row) => [...row])
    }

    static evaluateMove(
        board: TicTacToeProperty['board'],
        row: number, col: number,
        player: TicTacToeProperty['player'],
        streakToWin: number,
    ){
        for(const { x, y } of TicTacToe.directionList){
            if(board[row][col] !== player){
                continue
            }
            const streak = TicTacToe.calcStreak(board, row, col, { x, y }, player) +
                TicTacToe.calcStreak(board, row, col, { x: -x, y: -y }, player) - 1
            if (streak >= streakToWin){
                return 'W'
            }
        }
        return 'P'
    }

    evaluateMove(row: number, col: number){
        return TicTacToe.evaluateMove(this.board, row, col, this.player, this.streakToWin)
    }

    static evaluateBoard(
        board: TicTacToeProperty['board'],
        player: TicTacToeProperty['player'],
        streakToWin: number
    ){
        const evaluationBoard = TicTacToe.createBoardFromString('-'.repeat(board.length*board.length))
        for(var i = 0;i < board.length;i++){
            for(var j = 0;j < board.length;j++){
                evaluationBoard[i][j] = board[i][j]
                const pState = TicTacToe.evaluateMove(evaluationBoard, i, j, player, streakToWin)
                const oState = TicTacToe.evaluateMove(evaluationBoard, i, j, player === 'O'? 'X' : 'O', streakToWin)
                if(pState === 'W'){
                    return 'W'
                }
                if(oState === 'W'){
                    return 'L'
                }
            }
        }
        return 'P'
    }

    static calcStreak(
        board: TicTacToeProperty['board'],
        row: number, col: number,
        direc: { x: number, y: number },
        player: TicTacToeProperty['player']
    ): number{
        if(row < 0 || row >= board.length || col < 0 || col >= board.length || board[row][col] !== player){
            return 0
        }
        return 1 + TicTacToe.calcStreak(board, row + direc.x, col + direc.y, direc, player)
    }

    getMovedBoard(row: number, col: number){
        const newBoard = TicTacToe.cloneBoard(this.board)
        newBoard[row][col] = this.player
        return newBoard
    }

    move(row: number, col: number){
        this.board = this.getMovedBoard(row, col)
        this.gameState = this.evaluateMove(row, col)
    }

    static get90DegRotatedCell(x: number, y: number, width: number){
        return [y, width-x-1]
    }

    static get90DegRotatedBoard(board: TicTacToeProperty['board']){
        const newBoard = TicTacToe.cloneBoard(board)
        for(var i=0;i<board.length/2;i++){
            for(var j=i;j<board.length-i-1;j++){
                const [x1,y1] = [i,j]
                const c1 = newBoard[x1][y1]
                const [x2,y2] = TicTacToe.get90DegRotatedCell(x1, y1, board.length)
                const c2 = newBoard[x2][y2]
                const [x3,y3] = TicTacToe.get90DegRotatedCell(x2, y2, board.length)
                const c3 = newBoard[x3][y3]
                const [x4,y4] = TicTacToe.get90DegRotatedCell(x3, y3, board.length)
                const c4 = newBoard[x4][y4]
                newBoard[x1][y1] = c4
                newBoard[x2][y2] = c1
                newBoard[x3][y3] = c2
                newBoard[x4][y4] = c3
            }
        }
        return newBoard
    }

    static getReflectedBoard(board: TicTacToeProperty['board'], axis: 'H' | 'V'){
        const newBoard = TicTacToe.cloneBoard(board)
        for(var i=0;i<board.length/2;i++){
            const temp = [...newBoard[i]]
            newBoard[i] = [...newBoard[board.length-i-1]]
            newBoard[board.length-i-1] = temp
        }
        if(axis == 'V'){
            return TicTacToe.get90DegRotatedBoard(TicTacToe.get90DegRotatedBoard(newBoard))
        }
        return newBoard
    }

    static getEquivalentBoards(board: TicTacToeProperty['board']){
        let newBoard = TicTacToe.cloneBoard(board) 
        const equivalentBoards = new Set<string>()
        equivalentBoards.add(TicTacToe.boardToStrings(newBoard, true))
        equivalentBoards.add(TicTacToe.boardToStrings(TicTacToe.getReflectedBoard(newBoard, 'V'), true))
        equivalentBoards.add(TicTacToe.boardToStrings(TicTacToe.getReflectedBoard(newBoard, 'H'), true))
        newBoard = TicTacToe.get90DegRotatedBoard(newBoard)
        equivalentBoards.add(TicTacToe.boardToStrings(newBoard, true))
        newBoard = TicTacToe.get90DegRotatedBoard(newBoard)
        equivalentBoards.add(TicTacToe.boardToStrings(newBoard, true))
        newBoard = TicTacToe.get90DegRotatedBoard(newBoard)
        equivalentBoards.add(TicTacToe.boardToStrings(newBoard, true))
        const boardArray: string[] = []
        equivalentBoards.forEach((string) => boardArray.push(string))
        boardArray.sort((a, b) => a.length - b.length)
        return boardArray
    }

    static swapPlayerOnBoard(board: TicTacToeProperty['board']){
        const boardString = TicTacToe.boardToStrings(board, false)
        const swappedString = boardString.replace('X','ก').replace('O','X').replace('ก','O')
        return TicTacToe.createBoardFromString(swappedString)
    }

    static boardToStrings(board: TicTacToeProperty['board'], compressed = false){
        const string = board.flat().join('')
        return compressed? TicTacToe.compressBoardString(string) : string
    }

    static compressBoardString(boardString: string){
        let count = 0
        let char: string | null = null
        let compressString = ''
        for(var i=0;i<boardString.length;i++){
            if(!char){
                char = boardString[i]
            }
            else if(char != boardString[i]){
                if(count > 2){
                    compressString += `${count.toString(16)}${char}`
                } else {
                    compressString += char.repeat(count)
                }
                count = 1
                char = boardString[i]
                continue
            }
            count++
        }
        if(count > 2){
            compressString += `${count.toString(16)}${char}`
        } else {
            compressString += String(char).repeat(count)
        }
        return compressString
    }

    static decompressBoardString(compressedString: string){
        let numString = ''
        let boardString = ''
        for(var i=0;i<compressedString.length;i++){
            const c = compressedString[i]
            if(c === 'X' || c === 'O' || c === '-'){
                if(numString !== ''){
                    boardString += c.repeat(parseInt(numString, 16))
                    numString = ''
                    continue
                }
                boardString += c
            } else {
                numString += c
            }
        }
        return boardString
    }

    getReward(){
        switch(this.gameState){
            case 'W':
                return 100
            case 'L':
                return -100
            case 'P':
                return 0
        }
    }

    getAllNextStateActions(){
        const nextBoards: NextStateAction[] = []
        for(var i = 0;i < this.board.length;i++){
            for(var j = 0;j < this.board.length;j++){
                if(this.board[i][j] === '-'){
                    const newBoard = TicTacToe.cloneBoard(this.board)
                    newBoard[i][j] = this.player
                    nextBoards.push({ nextState: newBoard, action: { row: i, col: j }})
                }
            }
        }
        return nextBoards
    }

    static switchPlayer(player: 'X' | 'O'){
        return player === 'X'? 'O' : 'X'
    }
}