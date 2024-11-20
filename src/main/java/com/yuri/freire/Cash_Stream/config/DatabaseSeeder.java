package com.yuri.freire.Cash_Stream.config;

import com.yuri.freire.Cash_Stream.entities.ExpenseMethod;
import com.yuri.freire.Cash_Stream.entities.Recurrence;
import com.yuri.freire.Cash_Stream.entities.entity_enum.ExpenseMethodType;
import com.yuri.freire.Cash_Stream.entities.entity_enum.RecurrenceType;
import com.yuri.freire.Cash_Stream.entities.repositories.ExpenseMethodRespository;
import com.yuri.freire.Cash_Stream.entities.repositories.RecurrenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DatabaseSeeder implements CommandLineRunner {
    @Autowired
    private ExpenseMethodRespository expenseMethodRespository;

    @Autowired
    private RecurrenceRepository recurrenceRepository;

    @Override
    public void run(String... args) throws Exception{
        if(expenseMethodRespository.count() == 0){
            ExpenseMethod method1 = ExpenseMethod.builder()
                    .expenseMethodName(ExpenseMethodType.VA)
                    .build();

            ExpenseMethod method2 = ExpenseMethod.builder()
                    .expenseMethodName(ExpenseMethodType.VR)
                    .build();

            ExpenseMethod method3 = ExpenseMethod.builder()
                    .expenseMethodName(ExpenseMethodType.CREDIT)
                    .build();

            ExpenseMethod method4 = ExpenseMethod.builder()
                    .expenseMethodName(ExpenseMethodType.DEBIT)
                    .build();

            ExpenseMethod method5 = ExpenseMethod.builder()
                    .expenseMethodName(ExpenseMethodType.MONEY)
                    .build();

            expenseMethodRespository.saveAll(Arrays.asList(method1, method2, method3, method4, method5));
            System.out.println("Metodos de despesas inseridos");
        }

        if(recurrenceRepository.count() == 0){
            Recurrence recurrence1 = Recurrence.builder()
                    .recurrenceFrequency(RecurrenceType.MONTHLY)
                    .build();

            Recurrence recurrence2 = Recurrence.builder()
                    .recurrenceFrequency(RecurrenceType.BIMONTHLY)
                    .build();

            Recurrence recurrence3 = Recurrence.builder()
                    .recurrenceFrequency(RecurrenceType.QUARTERLY)
                    .build();

            Recurrence recurrence4 = Recurrence.builder()
                    .recurrenceFrequency(RecurrenceType.BIANNUAL)
                    .build();

            Recurrence recurrence5 = Recurrence.builder()
                    .recurrenceFrequency(RecurrenceType.ANNUAL)
                    .build();

            Recurrence recurrence6 = Recurrence.builder()
                    .recurrenceFrequency(RecurrenceType.NONRECURRING)
                    .build();

            recurrenceRepository.saveAll(Arrays.asList(recurrence1, recurrence2, recurrence3, recurrence4, recurrence5, recurrence6 ));
            System.out.println("Recurrence inseridos");
        }
    }
}
