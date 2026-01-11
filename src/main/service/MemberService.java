/**
 * AI:
 * regex
 * .stream().*
 *
 */

package main.service;

import main.model.Member;
import main.model.Book;
import main.exception.InvalidMemberException;
import main.exception.InvalidBookIdException;

import java.util.*;

public class MemberService {
    private List<Member> members;
    private int nextMemberId;

    public MemberService(List<Member> members) {
        this.members = members;
        this.nextMemberId = calculateNextMemberId();
    }

    private int calculateNextMemberId() {
        if (members.isEmpty()) {
            return 1;
        }

        return members.stream()
                .map(m -> Integer.parseInt(m.getMemberId()))
                .max(Integer::compareTo)
                .orElse(0) + 1;
    }

    public Member findMemberById(String memberId) throws InvalidMemberException {
        return members.stream()
                .filter(m -> m.getMemberId().equals(memberId))
                .findFirst()
                .orElseThrow(() -> new InvalidMemberException(
                        "Member ID '" + memberId + "' not found."
                ));
    }

    public boolean memberExists(String memberId) {
        return members.stream()
                .anyMatch(m -> m.getMemberId().equals(memberId));
    }

    public Member createMember(String name) throws InvalidMemberException {
        if (!isValidName(name)) {
            throw new InvalidMemberException(
                    "Invalid name: '" + name + "'. Names can only contain letters, spaces, and hyphens."
            );
        }

        if (nextMemberId > 999999) {
            throw new InvalidMemberException("Maximum member limit reached (999999).");
        }

        String memberId = String.format("%06d", nextMemberId);
        Member newMember = new Member(memberId,name);

        members.add(newMember);
        nextMemberId++;

        return newMember;
    }

    private boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }

        name = name.trim();

        if (name.length() < 2 || name.length() > 100) {
            return false;
        }

        String regex = "^[A-Za-zÁÉÍÓÖŐÚÜŰáéíóöőúüű\\s\\-]+$";

        if (!name.matches(regex)) {
            return false;
        }

        return name.matches(".*[A-Za-zÁÉÍÓÖŐÚÜŰáéíóöőúüű].*");
    }

    public boolean canMemberLoan(String memberId) throws InvalidMemberException {
        Member member = findMemberById(memberId);
        return member.canLoanMore();
    }

    public int getMemberLoanCount(String memberId) throws InvalidMemberException {
        Member member = findMemberById(memberId);
        return member.getLoanedBooks();
    }

    public void addBookToMember(String memberId, String bookId) throws InvalidMemberException {
        Member member = findMemberById(memberId);

        if (!member.canLoanMore()) {
            throw new InvalidMemberException("Member '" + memberId + "' has reached the maximum loan limit (3 books).");
        }

        member.addLoanedBook(bookId);
        member.incrementLoans();
    }

    public void removeBookFromMember(String memberId, String bookId) throws InvalidMemberException {
        Member member = findMemberById(memberId);

        if (!member.hasLoanedBook(bookId)) {
            throw new InvalidMemberException("Member '" + memberId + "' has not loaned book '" + bookId + "'.");
        }

        member.removeLoanedBook(bookId);
        member.decrementLoans();
    }

    public List<String> getMemberLoanedBookIds(String memberId) throws InvalidMemberException {
        Member member = findMemberById(memberId);
        return member.getLoanedBookIds();
    }

    public List<Book> getMemberLoanedBooks(String memberId, BookService bookService)
            throws InvalidMemberException {
        Member member = findMemberById(memberId);
        List<String> bookIds = member.getLoanedBookIds();
        List<Book> books = new ArrayList<>();

        for (String bookId : bookIds) {
            try {
                Book book = bookService.findBookById(bookId);
                books.add(book);
            } catch (InvalidBookIdException e) {
                System.err.println("Warning: Book " + bookId + " not found for member " + memberId);
            }
        }

        return books;
    }

    public boolean hasMemberLoanedBook(String memberId, String bookId) throws InvalidMemberException {
        Member member = findMemberById(memberId);
        return member.hasLoanedBook(bookId);
    }

    public void syncMemberLoanCounts() {
        for (Member member : members) {
            member.syncLoanCount();
        }
    }

    public void rebuildMemberLoanLists(List<String> loanBookIds, List<String> loanMemberIds) {
        for (Member member : members) {
            member.getLoanedBookIds().clear();
            member.syncLoanCount();
        }

        for (int i = 0; i < loanBookIds.size(); i++) {
            String bookId = loanBookIds.get(i);
            String memberId = loanMemberIds.get(i);

            try {
                Member member = findMemberById(memberId);
                member.addLoanedBook(bookId);
                member.incrementLoans();
            } catch (InvalidMemberException e) {
                System.err.println("Warning: Member " + memberId + " not found during sync");
            }
        }
    }

    public List<Member> getAllMembers() {
        return new ArrayList<>(members);
    }

    public List<Member> searchMembersByName(String name) {
        String searchTerm = name.toLowerCase().trim();

        return members.stream()
                .filter(m -> m.getName().toLowerCase().contains(searchTerm))
                .sorted(Comparator.comparing(Member::getName))
                .toList();
    }

    /**
     * STATISZTIKA: leggyakrabban késő tag
     */
    public Member getMemberWithMostFines(Map<String, Integer> memberFineCount) {
        if (memberFineCount.isEmpty()) {
            return null;
        }

        String mostFinesMemberId = memberFineCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        try {
            return findMemberById(mostFinesMemberId);
        } catch (InvalidMemberException e) {
            return null;
        }
    }

    public void deleteMember(String memberId) throws InvalidMemberException {
        Member member = findMemberById(memberId);

        if (member.getLoanedBooks() > 0) {
            throw new InvalidMemberException("Cannot delete member with active loans.");
        }

        members.remove(member);
    }

    public static boolean isValidMemberIdFormat(String memberId) {
        if (memberId == null) return false;

        if (!memberId.matches("\\d{6}")) {
            return false;
        }

        int id = Integer.parseInt(memberId);
        return id >= 1 && id <= 999999;
    }
}