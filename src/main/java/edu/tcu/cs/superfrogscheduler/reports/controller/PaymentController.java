package edu.tcu.cs.superfrogscheduler.reports.controller;

import edu.tcu.cs.superfrogscheduler.reports.converter.PaymentFormDtoToPaymentFormConverter;
import edu.tcu.cs.superfrogscheduler.reports.converter.PaymentFormToPaymentFormDtoConverter;
import edu.tcu.cs.superfrogscheduler.reports.dto.PaymentFormDto;
import edu.tcu.cs.superfrogscheduler.reports.dto.Period;
import edu.tcu.cs.superfrogscheduler.reports.dto.RequestIds;
import edu.tcu.cs.superfrogscheduler.reports.entity.PaymentForm;
import edu.tcu.cs.superfrogscheduler.reports.service.PaymentService;
import edu.tcu.cs.superfrogscheduler.system.Result;
import edu.tcu.cs.superfrogscheduler.system.StatusCode;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.endpoint.base-url}")
public class PaymentController {

    private PaymentService paymentService;

    private final PaymentFormToPaymentFormDtoConverter paymentFormToPaymentFormDtoConverter;

    private final PaymentFormDtoToPaymentFormConverter paymentFormDtoToPaymentFormConverter;


    public PaymentController(PaymentService paymentService, PaymentFormToPaymentFormDtoConverter paymentFormToPaymentFormDtoConverter, PaymentFormDtoToPaymentFormConverter paymentFormDtoToPaymentFormConverter) {
        this.paymentService = paymentService;
        this.paymentFormToPaymentFormDtoConverter = paymentFormToPaymentFormDtoConverter;
        this.paymentFormDtoToPaymentFormConverter = paymentFormDtoToPaymentFormConverter;
    }

    @GetMapping("/payment-forms")
    public Result getAllRequests() {
        List<PaymentForm> paymentForms = paymentService.getAllPaymentForms();
        List<PaymentFormDto> paymentFormDtos = paymentForms.stream()
                .map(this.paymentFormToPaymentFormDtoConverter::convert)
                .collect(Collectors.toList());

        return new Result(true, StatusCode.SUCCESS, "Find All Success", paymentFormDtos );
    }

    @PostMapping("/payment-forms")
    public Result generatePaymentForms(@RequestBody RequestIds requestIds) {

        List<String> selectedIds = requestIds.getRequestIds();

        Period paymentPeriod = requestIds.getPeriod();

        List<PaymentForm> paymentForms = this.paymentService.generatePaymentForms(selectedIds, paymentPeriod);
        List<PaymentFormDto> paymentFormDtos = paymentForms.stream()
                .map(this.paymentFormToPaymentFormDtoConverter::convert)
                .collect(Collectors.toList());

        return new Result(true, StatusCode.SUCCESS, "Payment forms are generated successfully.", paymentFormDtos);
    }

}
