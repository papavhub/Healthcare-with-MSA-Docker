package com.msa.diet.controller

import com.msa.diet.application.*
import com.msa.diet.domain.DietHistory
import com.msa.diet.domain.Food
import com.msa.diet.dto.DietDto
import com.netflix.discovery.converters.Auto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
class DietController(
        @Autowired private val foodSearchService: FoodSearchService,
        @Autowired private val createDietHistoryService: CreateDietHistoryService,
        @Autowired private val createFoodService:CreateFoodService,
        @Autowired private val deleteDietHistoryService: DeleteDietHistoryService,
        @Autowired private val getDietHistoryService: GetDietHistoryService
) {
    @GetMapping("/")
    fun search(
            @RequestHeader("username") username: String,
            @RequestParam("q") q: String
    ): ResponseEntity<List<Food>> {
        val foods = foodSearchService.search(q)
        return ResponseEntity(foods, HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    fun deleteDietHistory(
            @RequestHeader("username") username: String,
            @PathVariable("id") dietHistoryId: Long
    ): ResponseEntity<Unit> {
        deleteDietHistoryService.delete(username, dietHistoryId)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @PostMapping("/histories")
    fun createDietHistory(
            @RequestHeader("username") username: String,
            @RequestBody @Valid createDietHistoryReq: DietDto.CreateDietHistoryReq
    ): ResponseEntity<DietHistory> {
        val dietHistory = createDietHistoryService.create(username, createDietHistoryReq)
        return ResponseEntity(dietHistory, HttpStatus.CREATED)
    }

    @PostMapping("/food")
    fun createFood( //음식 추가 컨트롤러
            @RequestBody @Valid createFoodReq: DietDto.CreateFoodReq
    ): ResponseEntity<Food> {
        val food = createFoodService.create(createFoodReq)
        return ResponseEntity(food, HttpStatus.CREATED)
    }

    @GetMapping("/histories")
    fun getDietHistories(
            @RequestHeader("username") username: String,
            @RequestParam("period") period: Int
    ): ResponseEntity<List<DietHistory>> {
        val histories = getDietHistoryService.getHistories(username, period)
        return ResponseEntity(histories, HttpStatus.OK)
    }

    @GetMapping("/today")
    fun getDailyDiet(
            @RequestHeader("username") username: String
    ): ResponseEntity<List<DietHistory>> {
        val dailyDietHistories = getDietHistoryService.getDailyHistories(username)
        return ResponseEntity(dailyDietHistories, HttpStatus.OK)
    }
}