package com.example.myapplication.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Cat(
    val id: String,
    val name: String,
    @SerialName("alt_names")
    val alternativeName: String? = null,
    val description: String,
    val origin: String,
    val temperament: String,
    @SerialName("life_span")
    val lifeSpan: String,
    val weight: Weight,
    val adaptability: Int,
    @SerialName("affection_level")
    val affectionLevel: Int,
    @SerialName("child_friendly")
    val childFriendly: Int,
    @SerialName("dog_friendly")
    val dogFriendly: Int,
    @SerialName("energy_level")
    val energyLevel: Int,
    val intelligence: Int,
    val rare: Int,
    val image:CatImage? = null,
    @SerialName("wikipedia_url")
    val wikipediaUrl: String? = null
){
    fun allTemperaments(): List<String> {
        return this.temperament.replace(" ", "").split(",")
    }
}
@Serializable
data class CatImage(
    val width: Int,
    val height: Int,
    val url: String
)
@Serializable
data class Weight(
    val imperial: String,
    val metric: String
)

/*
{
    "weight": {
      "imperial": "7  -  10",
      "metric": "3 - 5"
    },
    "id": "abys",
    "name": "Abyssinian",
    "temperament": "Active, Energetic, Independent, Intelligent, Gentle",
    "origin": "Egypt",
    "description": "The Abyssinian is easy to care for, and a joy to have in your home. They’re affectionate cats and love both people and other animals.",
    "life_span": "14 - 15",
    "indoor": 0,
    "lap": 1,
    "alt_names": "",
    "adaptability": 5,
    "affection_level": 5,
    "child_friendly": 3,
    "dog_friendly": 4,
    "energy_level": 5,
    "grooming": 1,
    "health_issues": 2,
    "intelligence": 5,
    "shedding_level": 2,
    "social_needs": 5,
    "stranger_friendly": 5,
    "vocalisation": 1,
    "experimental": 0,
    "hairless": 0,
    "natural": 1,
    "rare": 0,
    "rex": 0,
    "suppressed_tail": 0,
    "short_legs": 0,
    "wikipedia_url": "https://en.wikipedia.org/wiki/Abyssinian_(cat)",
    "hypoallergenic": 0,
    "reference_image_id": "0XYvRd7oD",
    "image": {
      "id": "0XYvRd7oD",
      "width": 1204,
      "height": 1445,
      "url": "https://cdn2.thecatapi.com/images/0XYvRd7oD.jpg"
    }
  },
 */