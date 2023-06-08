package com.autoparts.controller;

import com.autoparts.dto.charts.LineChartDto;
import com.autoparts.serivce.ChartsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v3/chart")
@RequiredArgsConstructor
public class ChartController {

    private final ChartsService chartsService;

    @GetMapping("/sellsByMonth")
    public LineChartDto sellsByMonth() {
        return chartsService.sellsByMonth();
    }

    @GetMapping("/sellsByProduct")
    public LineChartDto findAll1() {
        return chartsService.sellsByProduct();
    }

    @GetMapping("/sellsByCategories")
    public LineChartDto getSellsGroupedByCategory() {
        return chartsService.getSellsGroupedByCategory();
    }

    @GetMapping("/sellsByBrands")
    public LineChartDto getSellsGroupedByBrands() {
        return chartsService.getSellsGroupedByBrands();
    }

    @GetMapping("/sellsTotalForMonth")
    public LineChartDto getSellsTotalForMonth() {
        return chartsService.getSellsTotalForMonth();
    }

    @GetMapping("/getRevenueByWeekForMonth")
    public LineChartDto getRevenueByWeekForMonth() {
        return chartsService.getRevenueByWeekForMonth();
    }

    @GetMapping("/monthSellsPredicted")
    public LineChartDto monthSellsPredicted() {
        return chartsService.monthSellsPredicted();
    }

}
