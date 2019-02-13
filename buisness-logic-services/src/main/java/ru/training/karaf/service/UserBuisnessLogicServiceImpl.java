package ru.training.karaf.service;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import ru.training.karaf.model.Book;
import ru.training.karaf.model.BookDO;
import ru.training.karaf.model.Feedback;
import ru.training.karaf.model.FeedbackDO;
import ru.training.karaf.model.User;
import ru.training.karaf.model.UserDO;
import ru.training.karaf.model.UserNameDO;
import ru.training.karaf.repo.BookRepo;
import ru.training.karaf.repo.UserRepo;

public class UserBuisnessLogicServiceImpl implements UserBuisnessLogicService {
    
    private UserRepo userRepo;
    private BookRepo bookRepo;

    public void setUserRepo(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public void setBookRepo(BookRepo bookRepo) {
        this.bookRepo = bookRepo;
    }
    
    @Override
    public List<? extends User> getAllUsers() {
        return userRepo.getAllUsers();
    }

    @Override
    public boolean createUser(User user) {
        if (userRepo.getUser(user.getLibCard()).isPresent()) {
            System.err.println("Lib card is already taken");
            return false;
        }
        
        if (!isUserDataValid(user)) {
            System.err.println("One or more parameters are invalid");
            return false;
        }
        
        UserDO userToCreate = new UserDO();
        userToCreate.setAddress(user.getAddress());
        userToCreate.setLibCard(user.getLibCard());
        userToCreate.setRegDate(user.getRegDate());
        userToCreate.setUserName(new UserNameDO(user.getUserName()));
        userRepo.createUser(userToCreate);
        return true;
    }

    @Override
    public boolean updateUser(String libCard, User user) {
        if (userRepo.getUser(user.getLibCard()).isPresent()
                && !libCard.equals(user.getLibCard())) {
            System.err.println("Lib card is already taken");
            return false;
        }
        if (!isUserDataValid(user)) {
            System.err.println("One or more parameters are invalid");
            return false;
        }
        try {
            UserDO userToUpdate = (UserDO)userRepo.getUser(libCard).get();
            userToUpdate.setAddress(user.getAddress());
            userToUpdate.setLibCard(user.getLibCard());
            userToUpdate.setRegDate(user.getRegDate());
            userToUpdate.setUserName(new UserNameDO(user.getUserName()));
            userRepo.updateUser(userToUpdate);
            return true;
        } catch (NoSuchElementException e) {
            System.err.println("User not found");
            return false;
        }
    }

    @Override
    public Optional<? extends User> getUser(String libCard) {
        return userRepo.getUser(libCard);
    }

    @Override
    public boolean deleteUser(String libCard) {
        userRepo.deleteUser(libCard);
        return true;
    }

    @Override
    public Set<? extends Book> getUserBooks(String libCard) {
        try {
            UserDO user = (UserDO)userRepo.getUser(libCard).get();
            return user.getBooks();
        } catch (NoSuchElementException e) {
            System.err.println("User not found");
            return Collections.EMPTY_SET;
        }
    }

    @Override
    public List<? extends Feedback> getUserFeedbacks(String libCard) {
        try {
            UserDO user = (UserDO)userRepo.getUser(libCard).get();
            return user.getFeedbacks();
        } catch (NoSuchElementException e) {
            System.err.println("User not found");
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public boolean addBook(String libCard, String title) {
        try {
            UserDO user = (UserDO)userRepo.getUser(libCard).get();
            BookDO book = (BookDO)bookRepo.getBook(title).get();
            if (user.getBooks().contains(book)) {
                System.err.println("User already has this book");
                return false;
            }
            user.getBooks().add(book);
            userRepo.updateUser(user);
            return true;
        } catch (NoSuchElementException e) {
            System.err.println("User/book not found");
            return false;
        }
    }

    @Override
    public boolean removeBook(String libCard, String title) {
        try {
            UserDO user = (UserDO)userRepo.getUser(libCard).get();
            BookDO book = (BookDO)bookRepo.getBook(title).get();
            if (!user.getBooks().remove(book)) {
                System.err.println("User doesn't have this book");
                return false;
            }
            userRepo.updateUser(user);
            return true;
        } catch (NoSuchElementException e) {
            System.err.println("User/book not found");
            return false;
        }
    }

    @Override
    public boolean addFeedback(String libCard, Feedback feedback) {
        try {
            if (feedback.getMessage() == null || feedback.getBook() == null) {
                System.err.println("One or more parameters are invalid");
                return false;
            }
            UserDO user = (UserDO)userRepo.getUser(libCard).get();
            BookDO book = (BookDO)
                    bookRepo.getBook(feedback.getBook().getTitle()).get();
            if (user.getBooks()
                    .stream()
                    .map(b -> b.getTitle())
                    .noneMatch(title ->
                            title.equals(feedback.getBook().getTitle()))) {
                
                System.err.println("User doens't have this book");
                return false;
            }
            
            if (user.getFeedbacks()
                    .stream()
                    .map(f -> f.getBook().getTitle())
                    .anyMatch(title ->
                            title.equals(feedback.getBook().getTitle()))) {
                
                System.err.println("User's feedback already exists");
                return false;
            }
            
            FeedbackDO fb = new FeedbackDO();
            fb.setMessage(feedback.getMessage());
            book.addFeedback(fb);
            user.addFeedback(fb);
            userRepo.updateUser(user);
            bookRepo.invalidateBook(book.getId());
            return true;
            
        } catch (NoSuchElementException e) {
            System.err.println("User/book not found");
            return false;
        }
    }

    @Override
    public boolean removeFeedback(String libCard, String title) {
        try {
            UserDO user = (UserDO)userRepo.getUser(libCard).get();
            BookDO book = (BookDO)bookRepo.getBook(title).get();
            for (FeedbackDO f: user.getFeedbacks()) {
                if (f.getBook().equals(book)) {
                    user.removeFeedback(f);
                    book.removeFeedback(f);
                    break;
                }
            }
            userRepo.updateUser(user);
            bookRepo.invalidateBook(book.getId());
            return true;
        } catch (NoSuchElementException e) {
            System.err.println("User/book not found");
            return false;
        }
    }
    
    private boolean isUserDataValid(User user) {
        return !(user.getAddress() == null || user.getLibCard() == null ||
                user.getRegDate() == null || user.getUserName() == null);
    } 
}
