import org.apache.commons.codec.digest.DigestUtils;
import java.util.*;

public class Task5 {
    public static void main(String[] args) {
        System.out.println("1.1 encrypt: " + encrypt("Hello"));
        System.out.println("1.2 decrypt: " + decrypt(new int[] {72, 33, -73, 84, -12, -3, 13, -13, -68}));
        System.out.println("2. canMove: " + canMove("Bishop", "A7", "G1"));
        System.out.println("3. canComplete: " + canComplete("buti","beautiful"));
        System.out.println("4. sumDigProd: " + sumDigProd(16,28));
        System.out.println("5. sameVowelGroup: " + sameVowelGroup(new String[] {"toe", "ocelot", "maniac"}));
        System.out.println("6. validateCard: " + validateCard(1234567890123452L));
        System.out.println("7.1 numToEng: " + numToEng(126));
        System.out.println("7.2 numToRu: " + numToRus(126));
        System.out.println("8. getSha256Hash: " + getSha256Hash("password123"));
        System.out.println("9. correctTitle: " + correctTitle("jO-nite SnoW, kINg IN thE noRth."));
        System.out.println("10. hexLattice: " + hexLattice(19));
    }
    // шифр
    // кодер
    public static List encrypt(String str) {
        ArrayList<Integer> list = new ArrayList<Integer>(); // список для вывода закодированного слова
        Stack<Integer> stack = new Stack<Integer>(); // стек для удобства работы
        for(char ch : str.toCharArray()) {
            if(stack.empty()) {
                // если стек пустой - добавляем аски код нашего символа для дальнейшего вычисления
                stack.push((int) ch);
                // и добавляем код нашего первого символа в список
                list.add((int) ch);
            } else {
                // если стек не пустой - был символ до этого, мы берем разность кода нашего символа и того, что в стеке и записываем в список
                list.add((int) ch - stack.pop());
                stack.push((int) ch);
            }
        }
        return list;
    }
    // декодер
    public static String decrypt(int[] arr) {
        StringBuilder sb = new StringBuilder();
        Stack<Integer> stack =  new Stack<Integer>();
        int num;
        for(int i : arr) {
            if(stack.empty()) {
                stack.push(i);
                sb.append((char) i);
            } else {
                sb.append((char) (i + stack.peek()));
                stack.push(stack.pop() + i);
            }
        }
        return sb.toString();
    }
    // шахматы
    public static boolean canMove(String figure, String start, String finish) {
        int sA = (int) start.charAt(0);
        int sI = Character.getNumericValue(start.charAt(1));
        int fA = (int) finish.charAt(0);
        int fI = Character.getNumericValue(finish.charAt(1));
        switch (figure.toLowerCase()) {
            case "king":
                return (Math.abs(sA-fA) < 2 && Math.abs(sI-fI) < 2);
            case "bishop":
                return (Math.abs(sA-fA) == Math.abs(sI-fI));
            case "rook":
                return (Math.abs(sA-fA) > 0 && sI==fI || Math.abs(sI-fI) > 0 && sA==fA);
            case "knight":
                return (Math.abs(sA-fA) == 2 && Math.abs(sI-fI) == 1 || Math.abs(sI-fI) == 2 && Math.abs(sA-fA) == 1);
            case "pawn":
                return (Math.abs(sI-fI) == 1 && sA==fA);
            case "queen":
                return (canMove("king", start, finish) || canMove("bishop", start, finish) || canMove("rook", start, finish));
            default:
                return false;
        }
    }

    // можно ли дополнить подстроку до строки
    public static boolean canComplete(String sString, String line) {
        Stack<Character> stack = new Stack<Character>(); // создаем массив для полного слова
        StringBuilder sb = new StringBuilder(sString);
        char alfa = sb.charAt(sb.length()-1); // берем для перебора последний символ подстроки
        // заполняем стек нашим целым словом
        for(char ch : line.toCharArray()) {
            stack.push(ch);
        }
        // пока не переберем все буквы нашей строки, будем проверять
        while(!stack.empty()) {
            // берем последний элемент строки и последний элемент подстроки, сравниваем
            if(alfa == stack.pop()) {
                // если равны, то мы удаляем этот последний символ из подстроки и потом проверяем строку на пустоту
                // если строка уже пустая - мы прошли всю подстроку без ошибок и возвращаем true
                sb.deleteCharAt(sb.length()-1);
                if(sb.length() == 0) {
                    return true;
                } else {
                     // если строка еще не пустая - продолжаем отсекать последние элементы и проверять
                    alfa = sb.charAt(sb.length()-1);
                }
            }
        }
        // если строка закончилась, а подстрока не опустела - нельзя подстроку дополнить
        return false;
    }
    // произведение до цифры
    public static int sumDigProd(int...number) {
        int sum = 0;
        int result = 1;
        int len; // длина числа
        for(int num : number) { // проходимся по всему начальному массиву и складываем все числа
            sum += num;
        }
        while(sum / 10 != 0) { // пока не дойдем до одной цифры
            len = String.valueOf(sum).length();
            for(int i = 1; i <= len; i++) { // проходимся по всем цифрам числа, перемножаем их
                result = result * (sum % 10);
                sum = sum / 10;
            }
            sum = result; // обновляем наше число для цикла
            result = 1; // возвращаем 1 для дальнейшего умножения в новом кругу
        }
        return sum;
    }
    // те же гласные
    public static List sameVowelGroup(String[] arr) {
        ArrayList<String> listArray = new ArrayList<String>(Arrays.asList(arr));
        ArrayList<Character> list = new ArrayList<Character>();
        String str;
        // добавляем в список все уникальные гласные первого слова
        for(char ch : arr[0].toCharArray()) {
            str = String.valueOf(ch);
            // проверяем, гласная ли буква
            if(str.matches("^(?i:[aeiouy]).*")) {
                if(list.size() == 0) {
                    list.add(ch);
                } else {
                    // проверка на содержание такого же символа уже в списке
                    boolean flag = true;
                    for(char c : list) {
                        if(ch == c) {
                            flag = false;
                            break;
                        }
                    }
                    // если такого же символа еще не было - добавляем его
                    if(flag) {
                        list.add(ch);
                    }
                }
            }
        }
        // проходимся по каждому слову
        for(int i = 1; i < listArray.size(); ++i) {
            boolean flag = false;
            // берем отдельно символы слова
            for(char ch : listArray.get(i).toCharArray()) {
                str = String.valueOf(ch);
                if(str.matches("^(?i:[aeiouy]).*")) {
                    //если гласный символ - проверяем, содержится ли в нашем первом слове
                    if(!list.contains(ch)) {
                        flag = true;
                        break;
                    }
                }
            }
            if(flag) {
                listArray.remove(listArray.get(i));
                i--;
            }
        }
        return listArray;
    }
    // номер кредитной карты
    public static boolean validateCard(long card) {
        StringBuilder sb = new StringBuilder(Long.toString(card));
        int sum = 0;
        int cont = Character.getNumericValue(sb.charAt(sb.length()-1));
        sb = sb.deleteCharAt(sb.length()-1); // удаление контрольной цифры
        sb = sb.reverse(); // переворачиваем номер
        if(sb.length() >= 14 && sb.length() <= 19) { // проверка на длину
            for(int i = 0; i < sb.length(); i++) {
                // удвоение нечетных позиций
                if((i+1) % 2 == 0) {
                    sum += Character.getNumericValue(sb.charAt(i));
                } else {
                    int number = Character.getNumericValue(sb.charAt(i));
                    if(number < 5) {
                        sum = sum + number*2;
                    } else {
                        sum = sum + (2*number) / 10 + (2*number) % 10;
                    }
                }
            }
            return 10 - sum % 10 == cont; // проверка с контрольным числом
        }
        return false;
    }
    // перевод
    public static String numToEng(int number) {
        HashMap<Integer, String> numToWord = new HashMap<>(){{
            put(0, "zero");
            put(1, "one");
            put(2, "two");
            put(3, "three");
            put(4, "four");
            put(5, "five");
            put(6, "six");
            put(7, "seven");
            put(8, "eight");
            put(9, "nine");
            put(10, "ten");
            put(11, "eleven");
            put(12, "twelve");
            put(13, "thirteen");
            put(14, "fourteen");
            put(15, "fifteen");
            put(16, "sixteen");
            put(17, "seventeen");
            put(18, "eighteen");
            put(19, "nineteen");
            put(20, "twenty");
            put(30, "thirty");
            put(40, "forty");
            put(50, "fifty");
            put(60, "sixty");
            put(70, "seventy");
            put(80, "eighty");
            put(90, "ninety");
            put(100, "one hundred");
            put(200, "two hundreds");
            put(300, "three hundreds");
            put(400, "four hundreds");
            put(500, "five hundreds");
            put(600, "six hundreds");
            put(700, "seven hundreds");
            put(800, "eight hundreds");
            put(900, "nine hundreds");
        }};

        if (numToWord.containsKey(number)) {
            return numToWord.get(number);
        } else {
            if (number > 100) {
                return String.format("%s %s", numToWord.get(number / 100 * 100), numToEng(number % 100));
            } else {
                return String.format("%s %s", numToWord.get(number / 10 * 10), numToEng(number % 10));
            }
        }
    }

    public static String numToRus(int number) {
        HashMap<Integer, String> numToWord = new HashMap<>(){{
            put(0, "ноль");
            put(1, "один");
            put(2, "два");
            put(3, "три");
            put(4, "четыре");
            put(5, "пять");
            put(6, "шесть");
            put(7, "семь");
            put(8, "восемь");
            put(9, "девять");
            put(10, "десять");
            put(11, "одинадцать");
            put(12, "двенадцать");
            put(13, "тренадцать");
            put(14, "четырнадцать");
            put(15, "пятнадцать");
            put(16, "шестнадцать");
            put(17, "семнадцать");
            put(18, "восемнадцать");
            put(19, "девятнацать");
            put(20, "двадцать");
            put(30, "тридцать");
            put(40, "сорок");
            put(50, "пятдесят");
            put(60, "шестьдесят");
            put(70, "семьдесят");
            put(80, "восемьдесят");
            put(90, "девятьдесят");
            put(100, "сто");
            put(200, "двести");
            put(300, "триста");
            put(400, "четыреста");
            put(500, "пятьсот");
            put(600, "шестьсот");
            put(700, "семьсот");
            put(800, "восемьсот");
            put(900, "девятьсот");
        }};

        if (numToWord.containsKey(number)) {
            return numToWord.get(number);
        } else {
            if (number > 100) {
                return String.format("%s %s", numToWord.get(number / 100 * 100), numToRus(number % 100));
            } else {
                return String.format("%s %s", numToWord.get(number / 10 * 10), numToRus(number % 10));
            }
        }
    }
    // хеш код
    public static String getSha256Hash(String message) {
        return DigestUtils.sha256Hex(message);
    }

    // заголовок в нужном регистре
    public static String correctTitle(String name) {
        ArrayList<String> ex = new ArrayList<String>(Arrays.asList("and", "the", "of", "in"));
        ArrayList<String> list = new ArrayList<String>(Arrays.asList(name.toLowerCase().split(" ")));
        StringBuilder sbRes = new StringBuilder();
        for(int k = 0; k < list.size(); k++) {
            StringBuilder sb = new StringBuilder(list.get(k));
            String ch;
            if(!ex.contains(sb.toString())) {
                ch = String.valueOf(sb.charAt(0)).toUpperCase();
                sb.replace(0,1,ch);
            }
            for(int i = 0; i < sb.length(); i++) {
                if((int) sb.charAt(i) == 45 && sb.length() >= 3 && i < sb.length()-2) { // если дефисное слово, дефис не одиночный и не в конце - оба слова с заглавной
                    ch = String.valueOf(sb.charAt(i+1)).toUpperCase();
                    sb.replace(i+1,i+2,ch);
                }
            }
            list.set(k, sb.toString());
            sbRes.append(sb.toString());
            sbRes.append(" ");
        }
        return sbRes.toString();
    }
    // число
    public static String hexLattice(int num) {
        int sum = 1;
        int count = 0;
        int wh;
        int minCount;
        int countIteration = 0;
        int maxLength;
        StringBuilder sb = new StringBuilder();
        while(sum < num) { // ближайшее такое число или оно же
            count += 6;
            sum += count;
        }
        if (sum == num) { // проверка на принадлежность числа к классу центрированных шестиугольных чисел
            sum = 0; // сумма для проверки, отрисовалась ли верхняя половина шестиугольника
            minCount = count / 6; // длина крайних строчек
            count = minCount;
            sb.append("\n");
            while(sum < num /2) { // считаем, сколько строчек будет в верхней половине
                sum += count;
                count ++;
                countIteration ++;
            }
            sum = 0;
            count = minCount;
            maxLength = minCount + countIteration;
            while(true) {
                count ++; // длина очередной строчки
                countIteration = maxLength - count; // количество пробелов
                wh = count;
                sum += count; // обновление количества точек
                while(countIteration > 0) {
                    sb.append(" ");
                    countIteration --;
                }
                while(wh > 0) { // отрисовка одной верхней строчки
                    sb.append("0 ");
                    wh --;
                }
                sb.append("\n");
                if(sum > num / 2) {
                    while(count > minCount+1) {
                        count --;
                        countIteration = maxLength - count;
                        wh = count;
                        while(countIteration > 0) {
                            sb.append(" ");
                            countIteration --;
                        }
                        while(wh > 0) { // отрисовка одной нижней строчки
                            sb.append("0 ");
                            wh --;
                        }
                        sb.append("\n");
                    }
                    break;
                } else {
                    if(num == 1) {
                        break;
                    }
                }
            }
            return sb.toString();
        }
        return "Invalid";
    }
}