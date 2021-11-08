package com.example.tictactoe


import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.R.array
import android.util.Log


class MainActivity : AppCompatActivity(), View.OnClickListener {

    // MIXIMAX
    object GFG {
        var player = 'X'
        var opponent = 'O'


        //Esta funcion devuelve true si quedan movimientos en el tablero y false si no.
        fun isMovesLeft(board: Array<CharArray>): Boolean {
            for (i in 0..2) {
                for (j in 0..2) {
                    if (board[i][j] == '_') {
                        return true
                    }
                }
            }
            return false
        }

        // esta funcion devuelve 10 si gana el jugador 1, -10 si pierde el jugador 1 o 0 si ninguno gana
        fun evaluate(b: Array<CharArray>): Int {
            // mira si hay victoria posible en las filas
            for (row in 0..2) {
                if (b[row][0] == b[row][1]
                    && b[row][1] == b[row][2]
                ) {
                    if (b[row][0] == player) {
                        return +10
                    } else if (b[row][0] == opponent) {
                        return -10
                    }
                }
            }

            // victoria posible en las columnas
            for (col in 0..2) {
                if (b[0][col] == b[1][col]
                    && b[1][col] == b[2][col]
                ) {
                    if (b[0][col] == player) {
                        return +10
                    } else if (b[0][col] == opponent) {
                        return -10
                    }
                }
            }

            // victoria posible en la diagonal
            if (b[0][0] == b[1][1] && b[1][1] == b[2][2]) {
                if (b[0][0] == player) {
                    return +10
                } else if (b[0][0] == opponent) {
                    return -10
                }
            }
            if (b[0][2] == b[1][1] && b[1][1] == b[2][0]) {
                if (b[0][2] == player) {
                    return +10
                } else if (b[0][2] == opponent) {
                    return -10
                }
            }

            // si no gana nadie devuelve 0
            return 0
        }

        // funcion recursiva
        fun minimax(
            board: Array<CharArray>,
            depth: Int, isMax: Boolean
        ): Int {
            val score = evaluate(board)


            if (score == 10) {
                return score
            }

            if (score == -10) {
                return score
            }

            if (isMovesLeft(board) == false) {
                return 0
            }

            if (isMax) {
                var best = -1000


                for (i in 0..2) {
                    for (j in 0..2) {

                        if (board[i][j] == '_') {

                            board[i][j] = player

                            best = Math.max(
                                best, minimax(
                                    board,
                                    depth + 1, !isMax
                                )
                            )

                            board[i][j] = '_'
                        }
                    }
                }
                return best
            } // If this minimizer's move
            else {
                var best = 1000

                // Traverse all cells
                for (i in 0..2) {
                    for (j in 0..2) {
                        // Check if cell is empty
                        if (board[i][j] == '_') {
                            // Make the move
                            board[i][j] = opponent

                            // Call minimax recursively and choose
                            // the minimum value
                            best = Math.min(
                                best, minimax(
                                    board,
                                    depth + 1, !isMax
                                )
                            )

                            // Undo the move
                            board[i][j] = '_'
                        }
                    }
                }
                return best
            }
        }

        // devuelve la mejor posicion posible
        fun findBestMove(board: Array<CharArray>): Move {
            var bestVal = -1000
            val bestMove = Move()
            bestMove.row = -1
            bestMove.col = -1

            // Traverse all cells, evaluate minimax function
            // for all empty cells. And return the cell
            // with optimal value.
            for (i in 0..2) {
                for (j in 0..2) {
                    // Check if cell is empty
                    if (board[i][j] == '_') {
                        // Make the move
                        board[i][j] = player

                        // compute evaluation function for this
                        // move.
                        val moveVal = minimax(board, 0, false)

                        // Undo the move
                        board[i][j] = '_'

                        // If the value of the current move is
                        // more than the best value, then update
                        // best/
                        if (moveVal > bestVal) {
                            bestMove.row = i
                            bestMove.col = j
                            bestVal = moveVal
                        }
                    }
                }
            }
            System.out.printf(
                "The value of the best Move "
                        + "is : %d\n\n", bestVal
            )
            return bestMove
        }


        class Move() {
            var row = 0
            var col = 0
        }
    }
// FIN MIXIMAX

    //COMPATIBILIDAD DEL JUEGO AL MIXIMAX

    fun fieldToBoard(): Array<CharArray> {
        val board = arrayOf(
            charArrayOf('_','_', '_'
            ),
            charArrayOf('_','_','_'
            ),
            charArrayOf('_','_', '_'
            )
        )

        val field = retField()
        for (i in 0 until field.size) {
            for (j in 0 until field.get(0).size) {
                if (field[i][j] == "X"){
                    board[i][j] = 'X'
                }else if (field[i][j] == "O"){
                    board[i][j] = 'O'
                }
            }
        }


        return board
    }

    fun retField(): Array<Array<String?>> {
        val field = Array(3) {
            arrayOfNulls<String>(
                3
            )
        }
        for (i in 0..2) {
            for (j in 0..2) {
                field[i][j] = buttons[i][j]!!.text.toString()
            }
        }
        return field
    }

    //JUEGO


    private val buttons = Array(3) {
        arrayOfNulls<Button>(
            3
        )
    }

    private var player1Turn = true

    private var roundCount = 0

    private var player1Points = 0
    private var player2Points = 0

    private var textViewPlayer1: TextView? = null
    private var textViewPlayer2: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textViewPlayer1 = findViewById(R.id.text_view_p1)
        textViewPlayer2 = findViewById(R.id.text_view_p2)
        for (i in 0..2) {
            for (j in 0..2) {
                val buttonID = "button_$i$j"
                val resID = resources.getIdentifier(buttonID, "id", packageName)
                buttons[i][j] = findViewById(resID)
                buttons[i][j]?.setOnClickListener(this)
            }
        }
        val buttonReset = findViewById<Button>(R.id.button_reset)
        buttonReset.setOnClickListener { resetGame() }

        var j = GFG.findBestMove(fieldToBoard()).col
        var i = GFG.findBestMove(fieldToBoard()).row
        buttons[i][j]?.setText("O")
    }

    override fun onClick(v: View) {
        if ((v as Button).text.toString() != "") {
            return
        }
        if (player1Turn) {
            v.setText("X")
        }


        roundCount++
        if (checkForWin()) {
            if (player1Turn) {
                player1Wins()
            } else {
                player2Wins()
            }
        } else if (roundCount == 9) {
            draw()
        } else {
            player1Turn = !player1Turn
        }
        if (!player1Turn){
            var j = GFG.findBestMove(fieldToBoard()).col
            var i = GFG.findBestMove(fieldToBoard()).row
            buttons[i][j]?.setText("O")
            player1Turn = !player1Turn
        }
    }

    private fun checkForWin(): Boolean {
        val field = retField()

        for (i in 0..2) {
            if (field[i][0] == field[i][1] && field[i][0] == field[i][2] && field[i][0] != "") {
                return true
            }
        }
        for (i in 0..2) {
            if (field[0][i] == field[1][i] && field[0][i] == field[2][i] && field[0][i] != "") {
                return true
            }
        }
        if (field[0][0] == field[1][1] && field[0][0] == field[2][2] && field[0][0] != "") {
            return true
        }
        return if (field[0][2] == field[1][1] && field[0][2] == field[2][0] && field[0][2] != "") {
            true
        } else false
    }

    private fun player1Wins() {
        player1Points++
        Toast.makeText(this, "PLAYER 1 WINS!", Toast.LENGTH_SHORT).show()
        updatePointsText()
        resetBoard()
    }

    private fun player2Wins() {
        player2Points++
        Toast.makeText(this, "BOT WINS!", Toast.LENGTH_SHORT).show()
        updatePointsText()
        resetBoard()
    }

    private fun draw() {
        Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show()
        resetBoard()
    }

    private fun updatePointsText() {
        textViewPlayer1!!.text = "Player 1: $player1Points"
        textViewPlayer2!!.text = "BOT: $player2Points"
    }

    private fun resetBoard() {
        for (i in 0..2) {
            for (j in 0..2) {
                buttons[i][j]!!.text = ""
            }
        }
        roundCount = 0
        player1Turn = true
    }

    private fun resetGame() {
        player1Points = 0
        player2Points = 0
        updatePointsText()
        resetBoard()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("roundCount", roundCount)
        outState.putInt("player1Points", player1Points)
        outState.putInt("player2Points", player2Points)
        outState.putBoolean("player1Turn", player1Turn)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        roundCount = savedInstanceState.getInt("roundCount")
        player1Points = savedInstanceState.getInt("player1Points")
        player2Points = savedInstanceState.getInt("player2Points")
        player1Turn = savedInstanceState.getBoolean("player1Turn")
    }


}

