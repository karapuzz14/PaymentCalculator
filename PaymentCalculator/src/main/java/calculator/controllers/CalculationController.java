package calculator.controllers;

import calculator.services.PaymentCalculationService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Optional;

@Controller
public class CalculationController {

    private final PaymentCalculationService calculationService;
    CalculationController(PaymentCalculationService calculationService)
    {
        this.calculationService = calculationService;
    }

    @GetMapping("/calculacte")
    String calculate(@RequestParam("salary") float salary,
                   @RequestParam("vacationSum") short vacationSum,
                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> startDate,
                     Model model)
    {

        float result;
        if(startDate.isEmpty())
            result = calculationService.calculatePaymentByAmount(salary, vacationSum);
        else
            result = calculationService.calculatePaymentByPeriod(salary, vacationSum, startDate.get());

        model.addAttribute("result", "Сумма отпускных: " + result + " руб.");

        return "index";
    }

}
