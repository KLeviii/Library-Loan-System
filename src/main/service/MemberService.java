/**
 * AI:
 * regex
 */

package main.service;

import main.model.Member;
import main.exception.InvalidMemberException;
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
        Member newMember = new Member(memberId, name);

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