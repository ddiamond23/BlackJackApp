package edu.heidelberg.finalproject

fun draw(): Int {
    return (1..11).random()
}

fun checkDealer(score: Int): Boolean {
    if (score > 21) {
        return true
    } else {
        return false
    }
}

fun calculateTotal(currentTotal: Int, drawTotal: Int): Int {
    return currentTotal + drawTotal
}

fun determineStatus(dealerScore: Int, playerScore: Int): Boolean {
    return if (playerScore >= dealerScore && playerScore < 21) {
        true
    } else if (playerScore == 21) {
        true
    } else {
        false
    }
}