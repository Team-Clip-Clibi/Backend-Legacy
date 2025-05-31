package boot.api.com.clip.api.matching.service;

import com.clip.api.matching.controller.dto.OneThingOrderDto;
import com.clip.api.matching.service.OneThingMatchingOrderService;
import com.clip.matching.entity.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

class OneThingMatchingOrderServiceTest {

    @DisplayName("토요일 원띵은 화요일까지 신청 가능하다.")
    @Test
    void createOrder() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Given
        LocalDate preferredDate = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY)); //원띵 신청 날짜
        LocalDate currentDate_tue = LocalDate.now().with(TemporalAdjusters.previous(DayOfWeek.TUESDAY));
        LocalDate currentDate_wen = LocalDate.now().with(TemporalAdjusters.previous(DayOfWeek.WEDNESDAY));

        OneThingOrderDto.Request request = OneThingOrderDto.Request.builder()
                .topic("topic")
                .district(OneThingDistrict.GANGNAM)
                .preferredDates(List.of(new UserOneThingMatching.PreferredDate(preferredDate, OneThingTimeSlot.DINNER)))
                .tmiContent("tmiContent")
                .oneThingBudgetRange(OneThingBudgetRange.MEDIUM)
                .oneThingCategory(OneThingCategory.HEALTH)
                .build();

        Method method = OneThingMatchingOrderService.class.getDeclaredMethod("isAvailableDate", OneThingOrderDto.Request.class, LocalDate.class);
        method.setAccessible(true);
        boolean result_tue = (boolean) method.invoke(null, request, currentDate_tue);
        boolean result_wen = (boolean) method.invoke(null, request, currentDate_wen);

        // Then
        Assertions.assertThat(result_tue).isTrue();
        Assertions.assertThat(result_wen).isFalse();

    }
}