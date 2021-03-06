package br.com.ope.repository

import br.com.ope.model.Professor
import io.micrometer.core.instrument.noop.NoopMeter
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ProfessorRepository : JpaRepository<Professor, UUID> {
    fun findAllByAtivoIsTrue() : MutableList<Professor>
    fun findAllByOrderByNome() : MutableList<Professor>
}