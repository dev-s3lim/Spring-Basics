package com.beyond.basic.b2_board.author.repository;

import com.beyond.basic.b2_board.author.domain.Author;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AuthorMemoryRepository {
    private List<Author> AuthorList = new ArrayList<>();
    public static Long id = 1L;

    public void save(Author author){
        this.AuthorList.add(author);
        id++;
    }

    public List<Author> findAll(){
        return this.AuthorList;
    }

    public Optional<Author> findById(Long id){
        return AuthorList.stream().filter(a -> a.getId() == id).findFirst();
    }

    public Optional<Author> findByEmail(String email){
        return AuthorList.stream().filter(a -> a.getEmail().equals(email)).findFirst();
    }

    public void delete(Long id){
        int index = -1;
        for (int i = 0; i< AuthorList.size(); i++){
           if (AuthorList.get(i).getId().equals(id)){
               index = i;
               break;
           }
        }
        AuthorList.remove(index);
    }
}
