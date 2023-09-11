package ru.HomeWork;

import java.util.Random;
import java.util.Scanner;

public class Main {

    private static final int WIN_COUNT = 4; // Выигрышная комбинация
    private static final char DOT_HUMAN = 'X'; // Фишка игрока - человек
    private static final char DOT_AI = '0'; // Фишка игрока - компьютер
    private static final char DOT_EMPTY = '*'; // Признак пустого поля

    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();

    private static char[][] field; // Двумерный массив хранит текущее состояние игрового поля

    private static int fieldSizeX; // Размерность игрового поля
    private static int fieldSizeY; // Размерность игрового поля


    public static void main(String[] args) {
        field = new char[5][];

        while (true){
            initialize();
            printField();
            while (true){
                humanTurn();
                printField();
                if (checkGameState(DOT_HUMAN, "Вы победили!"))
                    break;
                aiTurn();
                printField();
                if (checkGameState(DOT_AI, "Победил компьютер!"))
                    break;
            }
            System.out.print("Желаете сыграть еще раз? (Y - да): ");
            if (!scanner.next().equalsIgnoreCase("Y"))
                break;
        }
    }

    /**
     * Инициализация объектов игры
     */
    private static void initialize(){

        fieldSizeX = 5;
        fieldSizeY = 5;
        field = new char[fieldSizeX][fieldSizeY];
        for (int x = 0; x < fieldSizeX; x++){
            for (int y = 0; y < fieldSizeY; y++){
                field[x][y] = DOT_EMPTY;
            }
        }

    }

    /**
     * Отрисовка игрового поля
     *
     *     +-1-2-3-4-5-
     *     1|*|*|*|*|*|
     *     2|*|*|*|*|*|
     *     3|*|*|*|*|*|
     *     4|*|*|*|*|*|
     *     5|*|*|*|*|*|
     *     -------------
     */
    private static void printField(){
        System.out.print("+");
        for (int x = 0; x < fieldSizeX * 2 + 1; x++){
            System.out.print((x % 2 == 0) ? "-" : x / 2 + 1);
        }
        System.out.println();

        for (int x = 0; x < fieldSizeX; x++){
            System.out.print(x + 1 + "|");
            for (int y = 0; y < fieldSizeY; y++){
                System.out.print(field[x][y] + "|");
            }
            System.out.println();
        }

        for (int x = 0; x < fieldSizeX * 2 + 2; x++){
            System.out.print("-");
        }
        System.out.println();

    }

    /**
     * Обработка хода игрока (человек)
     */
    private static void humanTurn(){
        int x, y;

        do {

            while (true){
                System.out.print("Введите координату хода X (от 1 до 5): ");
                if (scanner.hasNextInt()){
                    x = scanner.nextInt() - 1;
                    scanner.nextLine();
                    break;
                }
                else {
                    System.out.println("Некорректное число, повторите попытку ввода.");
                    scanner.nextLine();
                }
            }

            while (true){
                System.out.print("Введите координату хода Y (от 1 до 5): ");
                if (scanner.hasNextInt()){
                    y = scanner.nextInt() - 1;
                    scanner.nextLine();
                    break;
                }
                else {
                    System.out.println("Некорректное число, повторите попытку ввода.");
                    scanner.nextLine();
                }
            }
        }
        while (!isCellValid(x, y) || !isCellEmpty(x, y));
        field[x][y] = DOT_HUMAN;
    }
    /**
     * Проверка, ячейка является пустой (DOT_EMPTY)
     * @param x
     * @param y
     * @return
     */
    private static boolean isCellEmpty(int x, int y){
        return field[x][y] == DOT_EMPTY;
    }
    /**
     * Проверка корректности ввода
     * (координаты хода не должны превышать размерность игрового поля)
     * @param x
     * @param y
     * @return
     */
    private static boolean isCellValid(int x, int y){
        return x >= 0 && x < fieldSizeX && y >= 0 && y < fieldSizeY;
    }

    /**
     * Обработка хода компьютера
     */
    private static void aiTurn(){
        int x, y;

        do {
            x = random.nextInt(fieldSizeX);
            y = random.nextInt(fieldSizeY);
        }
        while (!isCellEmpty(x, y));
        field[x][y] = DOT_AI;
    }

    /**
     * Проверка состояния игры
     * @param c фишка игрока
     * @param s победный слоган
     * @return
     */
    private static boolean checkGameState(char c, String s){
        if (checkWin2(c)) {
            System.out.println(s);
            return true;
        }
        if (checkDraw()) {
            System.out.println("Ничья!");
            return true;
        }

        return false; // Игра продолжается
    }

    /**
     * Проверка победы
     * @param c
     * @return
     */
    private static boolean checkWin2(char c){
        for (int x = 0; x < fieldSizeX; x++) {
            for (int y = 0; y < fieldSizeY; y++) {

                int back = fieldSizeX - WIN_COUNT + 1;  // Диапазон в котором не происходит ни одной проверки
                if (x >= back && y >= back) break;

                if (field[x][y] == c)
                    if (check_win(checkHorizontally(c, x, y)) ||
                            check_win(checkMainDiagonal(c, x, y)) ||
                            check_win(checkVertically(c, x, y)) ||
                            check_win(checkSecondaryDiagonal(c, x, y))) return true;
            }
        }

        return false;
    }

    /**
     * Проверка победы по количеству фишек
     * @param count количество фишек подряд
     * @return
     */
    private static Boolean check_win(int count){ return count == WIN_COUNT; }

    /**
     * Проверка фишек по вертикали
     * @param c
     * @param i
     * @param j
     * @return
     */
    private static int checkVertically(char c, int i, int j){
        if (i + WIN_COUNT <= fieldSizeX){

            int col = 1, x = i, y = j;
            while (true){

                x++;
                if (col != WIN_COUNT && field[x][y] == c)
                    col++;
                else return col;
            }
        }
        else return 1;
    }

    /**
     * Проверка фишек по направлению главной диагонали
     * @param c
     * @param i
     * @param j
     * @return
     */
    private static int checkMainDiagonal(char c, int i, int j){
        if (i + WIN_COUNT <= fieldSizeX && j + WIN_COUNT <= fieldSizeY){

            int col = 1, x = i, y = j;
            while (true){

                x++;
                y++;
                if (col != WIN_COUNT && field[x][y] == c)
                    col++;
                else return col;
            }
        }
        else return 1;
    }

    /**
     * Проверка фишек по горизонтали
     * @param c
     * @param i
     * @param j
     * @return
     */
    private static int checkHorizontally(char c, int i, int j){
        if (j + WIN_COUNT <= fieldSizeY){

            int col = 1, x = i, y = j;
            while (true){
                y++;
                if (col != WIN_COUNT && field[x][y] == c)
                    col++;
                else return col;
            }
        }
        else return 1;
    }

    /**
     * Проверка фишек по направлению побочной диагонали
     * @param c
     * @param i
     * @param j
     * @return
     */
    private static int checkSecondaryDiagonal(char c, int i, int j){
        if (i + WIN_COUNT <= fieldSizeX && j - WIN_COUNT >= -1){

            int col = 1, x = i, y = j;
            while (true){

                y--;
                x++;
                if (col != WIN_COUNT && field[x][y] == c)
                    col++;
                else return col;
            }
        }
        else return 1;
    }

    /**
     * Проверка на ничью
     * @return
     */
    private static boolean checkDraw(){
        for (int x = 0; x < fieldSizeX; x++){
            for (int y = 0; y < fieldSizeY; y++){
                if (isCellEmpty(x, y)) return false;
            }
        }
        return true;
    }

}