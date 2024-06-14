package edu.heidelberg.finalproject

// CPS 350 - Final Project
// Dani Diamond

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    var tries = 0
    var gameStatus = false
    var playerScore = 0
    var dealerScore = 0
    var betAmount = 25
    var playerBank = 200
    lateinit var dealerScoreString: String
    private lateinit var dealerOutput: TextView
    private lateinit var playerOutput: TextView
    private lateinit var bankOutput: TextView
    private lateinit var betOutput: TextView
    private lateinit var card1: ImageView
    private lateinit var card2: ImageView
    private lateinit var card3: ImageView
    private lateinit var optionsMenu: Menu
    private val positiveButtonClick = { dialog: DialogInterface, which: Int -> reset()}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dealerOutput = findViewById(R.id.dealerOutput)
        playerOutput = findViewById(R.id.playerOutput)
        bankOutput = findViewById(R.id.playerBank)
        betOutput = findViewById(R.id.betOutput)

        val bankStart = getString(R.string.d, playerBank)
        bankOutput.setText(bankStart)

        val betStart = getString(R.string.bet_d, betAmount)
        betOutput.setText(betStart)

        dealerScore = draw() + draw()
        dealerScoreString = dealerScore.toString()
        dealerOutput.setText(dealerScoreString)

        if (checkDealer(dealerScore)) {
            dealerLoss()
        }
    }

   override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.appbar_menu, menu)
        optionsMenu = menu!!
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val updateBet: String

        return when (item.itemId) {
            R.id.action_25 -> {
                betAmount = 25
                updateBet = getString(R.string.bet_d, betAmount)
                betOutput.setText(updateBet)
                true
            }
            R.id.action_50 -> {
                betAmount = 50
                updateBet = getString(R.string.bet_d, betAmount)
                betOutput.setText(updateBet)
                true
            }
            R.id.action_75 -> {
                betAmount = 75
                updateBet = getString(R.string.bet_d, betAmount)
                betOutput.setText(updateBet)
                true
            }
            R.id.action_100 -> {
                betAmount = 100
                updateBet = getString(R.string.bet_d, betAmount)
                betOutput.setText(updateBet)
                true
            }
            R.id.action_help -> {
                helpClick()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun drawClick(view: View?) {
        var cardValue = draw()
        playerScore = calculateTotal(playerScore, cardValue)
        var updatedScoreString = playerScore.toString()
        playerOutput.setText(updatedScoreString)

        tries++

        updatePictures(tries, cardValue)

        if (tries == 3) {
            gameStatus = determineStatus(dealerScore, playerScore)
            if (gameStatus) {
                winDialog()

            } else {
                playerLoss()
            }

            updateBank(gameStatus)
        }
    }

    fun finishClick(view: View) {
        if (determineStatus(dealerScore, playerScore)) {
            winDialog()
        } else {
            playerLoss()
        }
        updateBank(determineStatus(dealerScore, playerScore))
    }

    fun helpClick() {
        val intent = Intent(this, HelpActivity::class.java)
        startActivity(intent)
    }

    fun reset() {
        playerScore = 0
        playerOutput.setText(playerScore.toString())

        dealerScore = draw() + draw()
        dealerScoreString = dealerScore.toString()
        dealerOutput.setText(dealerScoreString)

        tries = 0

        val cardDrawable = ContextCompat.getDrawable(this, R.drawable.back)
        card1.setImageDrawable(cardDrawable)
        card2.setImageDrawable(cardDrawable)
        card3.setImageDrawable(cardDrawable)
    }

    fun updatePictures(tryNum: Int, cardNum: Int) {
        card1 = findViewById(R.id.image1)
        card2 = findViewById(R.id.image2)
        card3 = findViewById(R.id.image3)
        var imageId = R.drawable.back

        if (tryNum == 1) {
            imageId = when (cardNum) {
                1 -> R.drawable.ace
                2 -> R.drawable.two
                3 -> R.drawable.three
                4 -> R.drawable.four
                5 -> R.drawable.five
                6 -> R.drawable.six
                7 -> R.drawable.seven
                8 -> R.drawable.eight
                9 -> R.drawable.nine
                10 -> R.drawable.ten
                else -> R.drawable.king
            }
            val cardDrawable = ContextCompat.getDrawable(this, imageId)
            card1.setImageDrawable(cardDrawable)
        } else if (tryNum == 2) {
            imageId = when (cardNum) {
                1 -> R.drawable.ace
                2 -> R.drawable.two
                3 -> R.drawable.three
                4 -> R.drawable.four
                5 -> R.drawable.five
                6 -> R.drawable.six
                7 -> R.drawable.seven
                8 -> R.drawable.eight
                9 -> R.drawable.nine
                10 -> R.drawable.ten
                else -> R.drawable.king
            }
            val cardDrawable = ContextCompat.getDrawable(this, imageId)
            card2.setImageDrawable(cardDrawable)
        } else {
            imageId = when (cardNum) {
                1 -> R.drawable.ace
                2 -> R.drawable.two
                3 -> R.drawable.three
                4 -> R.drawable.four
                5 -> R.drawable.five
                6 -> R.drawable.six
                7 -> R.drawable.seven
                8 -> R.drawable.eight
                9 -> R.drawable.nine
                10 -> R.drawable.ten
                else -> R.drawable.king
            }
            val cardDrawable = ContextCompat.getDrawable(this, imageId)
            card3.setImageDrawable(cardDrawable)
        }
    }

    fun updateBank(status: Boolean) {
        if (status) {
            playerBank += betAmount
        } else {
            playerBank -= betAmount
        }

        val bankUpdate = getString(R.string.d, playerBank)
        bankOutput.setText(bankUpdate)
    }

    fun winDialog() {
        val builder = AlertDialog.Builder(this)

        with(builder) {
            setTitle("You won!")
            setMessage("You've won blackjack")
            setPositiveButton("Yay", positiveButtonClick)
            show()
        }
    }

    fun playerLoss() {
        val builder = AlertDialog.Builder(this)

        with(builder) {
            setTitle("You lost :(")
            setMessage("You've lost blackjack")
            setPositiveButton("Sad", positiveButtonClick)
            show()
        }
    }

    fun dealerLoss() {
        val builder = AlertDialog.Builder(this)

        with(builder) {
            setTitle("You won!")
            setMessage("The dealer's score is over 21")
            setPositiveButton("Sucker", positiveButtonClick)
            show()
        }
    }
}
