package edu.devdays

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class TrainingController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Training.list(params), model:[trainingCount: Training.count()]
    }

    def show(Training training) {
        respond training
    }

    def create() {
        respond new Training(params)
    }

    @Transactional
    def save(Training training) {
        if (training == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (training.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond training.errors, view:'create'
            return
        }

        training.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'training.label', default: 'Training'), training.id])
                redirect training
            }
            '*' { respond training, [status: CREATED] }
        }
    }

    def edit(Training training) {
        respond training
    }

    @Transactional
    def update(Training training) {
        if (training == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (training.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond training.errors, view:'edit'
            return
        }

        training.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'training.label', default: 'Training'), training.id])
                redirect training
            }
            '*'{ respond training, [status: OK] }
        }
    }

    @Transactional
    def delete(Training training) {

        if (training == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        training.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'training.label', default: 'Training'), training.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'training.label', default: 'Training'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
