package edu.devdays

class Training {

    String title
    String description
    String trainer
    String location
    Date   trainingDate

    static constraints = {
        title blank: false
        description blank: false
        trainer blank: false
        location blank: false
        trainingDate blank: false
    }
}
