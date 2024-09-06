package com.edigest.journalApp.services;
//controller will call the service
//this is best practrice
//Service will call reposotery

import com.edigest.journalApp.Reposotery.JournalEntryReposotery;
import com.edigest.journalApp.entity.JournalEntry;
import com.edigest.journalApp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

//@Service
@Component
public class JournalEntryService
{
     @Autowired()
       private JournalEntryReposotery journalEntryReposotery;

    @Autowired
    private UserService userService;


    @Transactional
    public void saveEntry(JournalEntry journalEntry, String userName)
    {
        User user=userService.findByUserName(userName);
        journalEntry.setDate(LocalDateTime.now());
        JournalEntry saved = journalEntryReposotery.save(journalEntry);
        user.getJournalEntris().add(saved);
        userService.saveEntry(user);
    }

    public void saveEntry(JournalEntry journalEntry)
    {
        journalEntryReposotery.save(journalEntry);
    }

    public List<JournalEntry> getAll()
    {
        return journalEntryReposotery.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId id)
    {
        return journalEntryReposotery.findById(id);
    }

    public void deleteById(ObjectId id, String userName)
    {
        User user=userService.findByUserName(userName);
        user.getJournalEntris().removeIf(x ->x.getId().equals(id));
        userService.saveEntry(user);
        journalEntryReposotery.deleteById(id);
    }
}
