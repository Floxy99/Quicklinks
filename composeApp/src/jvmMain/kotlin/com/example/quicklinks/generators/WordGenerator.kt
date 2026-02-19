package com.example.quicklinks.generators

import kotlin.random.Random


class WordGenerator {
    companion object{
        var randomWords = listOf<String>("fasdf", "fljöasdklf", "dfklaöjsdf")
        fun getRandomWord(): String{
            return randomWords[Random.nextInt(randomWords.size)]
        }

        fun getRandomNumber(): Int{
            return Random.nextInt()
        }
    }
}