package scannerVSbuffreader;//package scannerVSbuffreader;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class calculator {
    public static void main(String[] args) {
        System.out.println("Привет! Это программа МиниКалькулятор.");
        Scanner sc = new Scanner(System.in);

        System.out.print("Введите выражение: "); // пользователь вводит выражение
        String text = sc.nextLine();            // считываем в переменную text
        My_values mas = string_parser(text); // заносим введеные данные в массив чисел и массив операндов

        int result = func_calc(mas); //  вызываем функцию подсчета выражения
        System.out.println(result); // вывод результата на экран
    }

    // входное выражение разбиваем на два массива: операторы и операнды
    public static My_values string_parser(String new_expr) {
        new_expr = new_expr.replaceAll("\\s", ""); //Удаляет пробелы
        // Максимальная длина строки - 10 (три числа не более 10 и 2 операции и знак перед первым числом)
        if (new_expr.length()>10) {
            throw new RuntimeException("Превышено количество цифр и/или операций");
        }

        List<Integer> mas_number = new ArrayList<>();
        List<String> mas_operands = new ArrayList<>();

        int number_step =0;
        int operands_step =0;
        int i = 0;

        while (i < new_expr.length()) {
            char c = new_expr.charAt(i);
            // если встречаем цифру, то формируем число
            if (c >= '0' && c <= '9') {
                int sign =0;                                    // для определения знака минус перед числом
                if ((i!=0) && (new_expr.charAt(i-1) == '-' )) sign = 1;
                StringBuilder sb = new StringBuilder();
                do {
                    sb.append(c);
                    i++;
                    if (i >= new_expr.length()) {
                        break;
                    }
                    c = new_expr.charAt(i);
                } while (c >= '0' && c <= '9');

                int  number = Integer.parseInt(String.valueOf(sb));

                if (10 < number){
                    throw new RuntimeException("Значение числа не более 10 ");
                }

                // в массив пишем число со знаком
                if (sign == 1) mas_number.add(-number);
                else mas_number.add(number);
                number_step ++;
                }

            else {
                switch (c) {
                    case '-': {
                        if ((i==0) && (new_expr.charAt(0) == '-')) { // первый минус не пишем
                            i++;
                            break;
                        }
                    }
                    case '+':
                    case '*':
                    case '/': {
                        mas_operands.add(String.valueOf(c));
                        operands_step++;
                        i++;
                        break;
                    }
                    default:
                        throw new RuntimeException("Некорректный символ");
                }
            }

        }
        if ((mas_number.size() <2) || mas_operands.size() <1)
            throw new RuntimeException("Недостаточно данных");
        
        return new My_values(mas_number, mas_operands);
    }
    public static int func_calc(My_values mas){

        List<String> operands = mas.getItemOperators();
        List<Integer> numbers = mas.getItemNumbers();
        int result = 0;
        String op1 = operands.get(0);

        // если первый оператор * или /, то выполняем последовательно, чтобы исключить ситуацюю, когда два оператора - деление
        if ((numbers.size() == 2) || (op1.equals("*")) || (op1.equals("/"))) {
            result = numbers.get(0);
            for (int i = 0; i < operands.size(); i++)
                switch (operands.get(i)) {
                    case "*": {
                        result *= numbers.get(i + 1);
                        break;
                    }
                    case "/": {
                        result /= numbers.get(i + 1);
                        break;
                    }
                    case "+":
                    case "-": {
                        result += numbers.get(i + 1);
                        break;
                    }
                }
        }else {
                result = numbers.get(2);
                for (int i=1; i >= 0 ; i--) {
                    switch (operands.get(i)) {
                        case "*": {
                            result *= numbers.get(i);
                            break;
                        }
                        case "/": {
                            result = numbers.get(i) / result;
                            break;
                        }
                        case "+":
                        case "-": {
                            result = numbers.get(i) + result;
                            break;
                        }
                    }

            }
        }
        return result;
    }
}

class My_values {
    private List<Integer> itemNumbers; //array2
    private List<String> itemOperators; //array1

    public My_values(List<Integer> itemNumbers, List<String> itemOperators)
    {
        this.itemNumbers = itemNumbers;
        this.itemOperators = itemOperators;
    }

    public List<String> getItemOperators() {return itemOperators; }
    public List<Integer> getItemNumbers() {return itemNumbers;}
}
