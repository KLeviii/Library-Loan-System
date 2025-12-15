package main.ui;

import main.service.*;
import static main.ui.AnsiColors.*;

import java.util.Scanner;

public class MenuUI {
    private Scanner scanner;
    private BookService bookService;
    private MemberService memberService;
    private LoanService loanService;

    // TODO: constructor and ansi call methods

    public void println(String value) {
        System.out.println(value);
    }

    public void print(String value) {
        System.out.print(value);
    }

}
