package maze;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main {
    private static final String PROMPT = "Enter the size of a new maze";
    private static final String FILE = "C:\\ASCII_Animals\\maze.txt";
    private static final String FIRST_MENU = """
            === Menu ===
            1. Generate a new maze
            2. Load a maze
            0. Exit""";
    private static final String SECOND_MENU = """
            === Menu ===
            1. Generate a new maze
            2. Load a maze
            3. Save the maze
            4. Display the maze
            5. Find the escape
            0. Exit""";
    private static final String BYE = "Bye!";
    private static final String INCORRECT_INPUT = "Incorrect option. Please try again";
    private static final String FILE_NOT_EXIST = "The file ... does not exist";
    private static final String INVALID_FORMAT = "Cannot load the maze. It has an invalid format";
    static Maze maze;
    static boolean isMazeExist = false;

    public static void main(String[] args) throws Exception {
        boolean isFileExist = new File(FILE).isFile();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            printMenu(isFileExist);
            String choice = scanner.nextLine();
            switch (choice) {
                case "0" -> {
                    System.out.println(BYE);
                    return;
                }
                case "1" -> generateNewMaze(scanner);
                case "2" -> loadMaze();
                case "3" -> choice3SaveOrNothing();
                case "4" -> choice4PrintOrNothing();
                case "5" -> choice5FindTheEscapeOrNothing();
                default -> System.out.println(INCORRECT_INPUT);

            }
        }
    }

    private static void choice5FindTheEscapeOrNothing() {
        if (isMazeExist) {
            maze.findTheEscape();
            maze.printField();
        }
    }

    private static void loadMaze() throws FileNotFoundException {
        boolean isFileExist = new File(FILE).isFile();
        int mazeSize;
        String mazeInString;
        if (!isFileExist) {
            System.out.println(FILE_NOT_EXIST + "1");
            return;
        }

        try (Scanner s = new Scanner(new File(FILE))) {
            if (s.hasNext()) {
                try {
                    mazeSize = Integer.parseInt(s.nextLine());
                } catch (Exception e) {
                    System.out.println(INVALID_FORMAT + "2");
                    return;
                }
            } else {
                System.out.println(INVALID_FORMAT + "3");
                return;
            }

            if (s.hasNext()) {
                try {
                    mazeInString = s.nextLine();
                } catch (Exception e) {
                    System.out.println(INVALID_FORMAT + "4");
                    return;
                }
            } else {
                System.out.println(INVALID_FORMAT + "5");
                return;
            }

            if (mazeSize * mazeSize != mazeInString.length()) {
                System.out.println(INVALID_FORMAT + "6");
                return;
            }

            String[] mazeArray = mazeInString.split("");
            maze = new Maze(mazeSize - 1,mazeSize - 1);

            try {
                for (int x = 0; x < mazeArray.length; x++) {
                    int row = x / mazeSize;
                    int col = x - row * mazeSize;
                    maze.field[row][col] = Integer.parseInt(mazeArray[x]);
                }
            } catch (Exception e) {
                System.out.println(INVALID_FORMAT + "7");
                return;
            }
        }
        isMazeExist = true;
    }

    private static void choice3SaveOrNothing() throws Exception {
        if (isMazeExist) {
            saveMaze();
        }
    }

    private static void saveMaze() throws Exception {
        StringBuilder fileContent = new StringBuilder();
        try (PrintWriter writer = new PrintWriter(FILE, StandardCharsets.UTF_8)) {
            int mazeSize = maze.field.length;
            fileContent.append(mazeSize);
            fileContent.append("\n");
            for (int y = 0; y < mazeSize; y++) {
                for (int x = 0; x < mazeSize; x++) {
                    fileContent.append(maze.field[y][x]);
                }
            }
            writer.print(fileContent);
            System.out.println(FILE);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    private static void choice4PrintOrNothing() {
        if (isMazeExist) {
            maze.printField();
        }
    }

    private static void generateNewMaze(Scanner scanner) {
        System.out.println(PROMPT);
        int sizeMaze = Integer.parseInt(scanner.nextLine());
        maze = new Maze(sizeMaze,sizeMaze);
        isMazeExist = true;
        maze.printField();
    }

    private static void printMenu(boolean isFileExist) {
        if (isFileExist || isMazeExist) {
            System.out.println(SECOND_MENU);
        } else {
            System.out.println(FIRST_MENU);
        }
    }
}

