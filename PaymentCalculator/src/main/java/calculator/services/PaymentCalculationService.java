package calculator.services;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс-сервис, содержащий логику расчёта отпускных
 */
@Service
public class PaymentCalculationService
{
    private final static float AVERAGE_DAY_IN_MONTH_NUMBER = 29.3f;

    public final static int CURRENT_YEAR = LocalDate.now().getYear();

    private final static float TAX_PERCENT = 0.13f;

    /**
     * Функция расчёта количества праздничных дней в отпуске
     * @return Список официальных праздников
     */
    private List<LocalDate> getHolidays()
    {
        List<LocalDate> holidays = new ArrayList<>();
        holidays.add(LocalDate.of(CURRENT_YEAR, 1, 1));
        holidays.add(LocalDate.of(CURRENT_YEAR, 1, 2));
        holidays.add(LocalDate.of(CURRENT_YEAR, 1, 3));
        holidays.add(LocalDate.of(CURRENT_YEAR, 1, 4));
        holidays.add(LocalDate.of(CURRENT_YEAR, 1, 5));
        holidays.add(LocalDate.of(CURRENT_YEAR, 1, 6));
        holidays.add(LocalDate.of(CURRENT_YEAR, 1, 7));
        holidays.add(LocalDate.of(CURRENT_YEAR, 1, 8));
        holidays.add(LocalDate.of(CURRENT_YEAR, 2, 23));
        holidays.add(LocalDate.of(CURRENT_YEAR, 3, 8));
        holidays.add(LocalDate.of(CURRENT_YEAR, 5, 1));
        holidays.add(LocalDate.of(CURRENT_YEAR, 5, 9));
        holidays.add(LocalDate.of(CURRENT_YEAR, 6, 12));
        holidays.add(LocalDate.of(CURRENT_YEAR, 11, 4));

        return holidays;
    }
    /**
     * Функция расчёта количества праздничных дней в отпуске
     * @param startDate Дата первого дня отпуска
     * @param vacationSum Количество дней в отпуске
     * @return Количество праздничных дней в отпуске
     */
    private short calculateUnpaidDays(LocalDate startDate, short vacationSum)
    {
        short holidaySum = 0;
        List<LocalDate> holidays = getHolidays();

        for(int i = 0; i < vacationSum; i++)
        {
            if (holidays.contains(startDate.plusDays(i)))
                holidaySum++;
        }

        return holidaySum;
    }

    /**
     * Функция расчёта отпускных по количеству дней без учёта праздников
     * @param salary Средняя зарплата за 12 месяцев
     * @param vacationSum Количество дней в отпуске
     * @return Сумма отпускных
     */
    public float calculatePaymentByAmount(float salary, short vacationSum)
    {

        // среднедневной заработок при условии, что все дни за расчетный период
        // были отработаны и ни один из дней отпуска не является официальным праздником
        float salaryPerDay = salary / (12 * AVERAGE_DAY_IN_MONTH_NUMBER);

        return salaryPerDay * vacationSum * (1f - TAX_PERCENT);
    }

    /**
     * Функция расчёта отпускных за конкретный период с учётом праздников
     * @param salary Средняя зарплата за 12 месяцев
     * @param vacationSum Количество дней в отпуске
     * @param startDate Дата первого дня отпуска
     * @return Сумма отпускных
     */
    public float calculatePaymentByPeriod(float salary, short vacationSum, LocalDate startDate)
    {

        // количество праздничных дней, приходящихся на отпуск
        int holidaySum = calculateUnpaidDays(startDate, vacationSum);

        // среднедневной заработок при условии, что все дни за расчетный период являются отработанными
        float salaryPerDay = salary / (12f * AVERAGE_DAY_IN_MONTH_NUMBER);

        return salaryPerDay * (vacationSum - holidaySum) * (1f - TAX_PERCENT);
    }


}
