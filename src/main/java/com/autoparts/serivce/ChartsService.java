package com.autoparts.serivce;

import com.autoparts.dto.charts.DatasetDto;
import com.autoparts.dto.charts.LineChartDto;
import com.autoparts.dto.ml.PredictResultDto;
import com.autoparts.dto.ml.PredictionResponse;
import com.autoparts.repository.TovarRepository;
import com.autoparts.repository.order.OrderRepository;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChartsService {

    private final OrderRepository orderRepository;
    private final TovarRepository tovarRepository;

    public LineChartDto sellsByMonth(){
        List<Tuple> orderGroupedByMonth = orderRepository.getOrderGroupedByMonth();

        LineChartDto dataDto = new LineChartDto();
        DatasetDto datasetDto = new DatasetDto();
        datasetDto.setLabel("Количество продаж товара в день");
        datasetDto.setBackgroundColor("blue");

        List<String> dates = orderGroupedByMonth.stream()
                .map(tuple -> tuple.get(0, LocalDate.class))
                .map(dateTime -> dateTime.format(DateTimeFormatter.ISO_DATE))
                .collect(toList());

        List<String> counts = orderGroupedByMonth.stream()
                .map(tuple -> tuple.get(1, Long.class)).filter(Objects::nonNull)
                .map(Object::toString)
                .collect(toList());

        dataDto.setLabels(dates);
        datasetDto.setData(counts);
        dataDto.setDatasets(singletonList(datasetDto));
        return dataDto;
    }

    public LineChartDto sellsByProduct(){
        List<Tuple> orderGroupedByMonth = orderRepository.getOrderGroupedByProduct();

        LineChartDto dataDto = new LineChartDto();
        DatasetDto datasetDto = new DatasetDto();
        datasetDto.setLabel("Товар-бестселлер");
        datasetDto.setBackgroundColor("blue");

        List<String> dates = orderGroupedByMonth.stream()
                .map(tuple -> tuple.get(0,String.class))
                .collect(toList());

        List<String> counts = orderGroupedByMonth.stream()
                .map(tuple -> tuple.get(1, Long.class)).filter(Objects::nonNull)
                .map(Object::toString)
                .collect(toList());

        dataDto.setLabels(dates);
        datasetDto.setData(counts);
        dataDto.setDatasets(singletonList(datasetDto));
        return dataDto;
    }

    public LineChartDto getSellsGroupedByCategory(){
        List<Tuple> sellsGroupedByCategory = orderRepository.getSellsGroupedByCategory();

        LineChartDto dataDto = new LineChartDto();
        DatasetDto datasetDto = new DatasetDto();
        datasetDto.setLabel("Категория-бестселлер");
        datasetDto.setBackgroundColor("blue");

        List<String> names = sellsGroupedByCategory.stream()
                .map(tuple -> tuple.get(0, String.class)).toList();

        List<String> counts = sellsGroupedByCategory.stream()
                .map(tuple -> tuple.get(1, Long.class)).filter(Objects::nonNull)
                .map(Object::toString)
                .collect(toList());

        dataDto.setLabels(names);
        datasetDto.setData(counts);
        dataDto.setDatasets(singletonList(datasetDto));
        return dataDto;
    }

    public LineChartDto getSellsGroupedByBrands(){
        List<Tuple> sellsGroupedByCategory = orderRepository.getSellsGroupedByBrands();

        LineChartDto dataDto = new LineChartDto();
        DatasetDto datasetDto = new DatasetDto();
        datasetDto.setLabel("Бренд-бестселлер");
        datasetDto.setBackgroundColor("blue");

        List<String> names = sellsGroupedByCategory.stream()
                .map(tuple -> tuple.get(0, String.class)).toList();

        List<String> counts = sellsGroupedByCategory.stream()
                .map(tuple -> tuple.get(1, Long.class)).filter(Objects::nonNull)
                .map(Object::toString)
                .collect(toList());

        dataDto.setLabels(names);
        datasetDto.setData(counts);
        dataDto.setDatasets(singletonList(datasetDto));
        return dataDto;
    }

    public LineChartDto getSellsTotalForMonth(){
        List<Tuple> sellsGrouped = orderRepository.getSellsTotalForMonth();

        LineChartDto dataDto = new LineChartDto();
        DatasetDto datasetDto = new DatasetDto();
        datasetDto.setLabel("Cумма продаж за дней");
        datasetDto.setBackgroundColor("blue");


        List<String> dates = sellsGrouped.stream()
                .map(tuple -> tuple.get(0, LocalDate.class))
                .map(dateTime -> dateTime.format(DateTimeFormatter.ISO_DATE))
                .collect(toList());

        List<String> counts = sellsGrouped.stream()
                .map(tuple -> tuple.get(1, Double.class)).filter(Objects::nonNull)
                .map(Object::toString)
                .collect(toList());

        dataDto.setLabels(dates);
        datasetDto.setData(counts);
        dataDto.setDatasets(singletonList(datasetDto));
        return dataDto;
    }

    public LineChartDto getRevenueByWeekForMonth(){
        List<Object[]> revenueByWeekForMonth = orderRepository.getRevenueByDayForMonth();

        LineChartDto dataDto = new LineChartDto();
        ArrayList<DatasetDto> datasets = new ArrayList<>();
        DatasetDto datasetDto = new DatasetDto();
        DatasetDto datasetDto2 = new DatasetDto();
        datasetDto.setLabel("Выручка за дней");
        datasetDto2.setLabel("Сумма продаж за дней");
        datasetDto.setBackgroundColor("blue");
        datasetDto2.setBackgroundColor("red");


        Map<String, Double> weeklyTotals = revenueByWeekForMonth.stream()
                .collect(Collectors.toMap(
                        result -> ((java.sql.Timestamp) result[0]).toLocalDateTime().toLocalDate().toString(),
                        result ->  Double.valueOf(result[1].toString()))
                );

        List<String> dates = weeklyTotals.keySet().stream().toList();
        List<String> counts = weeklyTotals.values().stream().map(s-> String.valueOf(s * 0.2)).toList();
        List<String> counts2 = weeklyTotals.values().stream().map(String::valueOf).toList();

        dataDto.setLabels(dates);
        datasetDto.setData(counts);
        datasetDto2.setData(counts2);
        datasets.add(datasetDto);
        datasets.add(datasetDto2);
        dataDto.setDatasets(datasets);
        return dataDto;
    }

    public LineChartDto monthSellsPredicted(){

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<PredictionResponse> response = restTemplate.postForEntity("http://172.17.0.4:5000/predict",null, PredictionResponse.class);
        PredictionResponse predictionResponse = response.getBody();
        List<PredictResultDto> results = predictionResponse.getPrediction();


        LineChartDto dataDto = new LineChartDto();
        ArrayList<DatasetDto> datasets = new ArrayList<>();
        DatasetDto datasetDto = new DatasetDto();
        DatasetDto datasetDto2 = new DatasetDto();
        datasetDto.setLabel("Прогноз продаж товара на следующие 30 дней");
        datasetDto2.setLabel("Количество проданных товаров");
        datasetDto.setBackgroundColor("blue");
        datasetDto2.setBackgroundColor("red");

        List<String> dates = results.stream()
                .map(PredictResultDto::getSellsForMonth)
                .map(Object::toString)
                .collect(toList());


        List<Long> ids = results.stream()
                .map(PredictResultDto::getId).toList();

        List<String> counts = tovarRepository.findProductNamesByIdIN(ids);

        List<String> data2 = orderRepository.findTovarSalesForCurrentMonth(ids)
                .stream()
                .map(Object::toString)
                .collect(toList());

        dataDto.setLabels(counts);
        datasetDto.setData(dates);
        datasetDto2.setData(data2);
        datasets.add(datasetDto2);
        datasets.add(datasetDto);
        dataDto.setDatasets(datasets);
        return dataDto;
    }


}
