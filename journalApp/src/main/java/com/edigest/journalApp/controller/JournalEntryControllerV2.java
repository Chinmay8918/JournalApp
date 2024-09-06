package com.edigest.journalApp.controller;

import com.edigest.journalApp.entity.JournalEntry;
import com.edigest.journalApp.entity.User;
import com.edigest.journalApp.services.JournalEntryService;
import com.edigest.journalApp.services.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2
{


    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;


    @GetMapping("{userName}")
    public ResponseEntity<?> getAllJournalEntriesOfUsers(@PathVariable String userName)
    {
        User user = userService.findByUserName(userName);
        List<JournalEntry> all= user.getJournalEntris();
        if(all != null && !all.isEmpty())
        {
            return new ResponseEntity<>(all,HttpStatus.OK);
        }
     return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("{userName}")
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry,@PathVariable String userName)
    {
        try
        {
            journalEntryService.saveEntry(myEntry,userName);
            return new ResponseEntity<>(myEntry,HttpStatus.CREATED);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("id/{myID}")
    public ResponseEntity<?> getJournalEntryByID(@PathVariable ObjectId myID)
    {
          Optional<JournalEntry> journalEntry= journalEntryService.findById(myID);
          if(journalEntry.isPresent())
          {
              return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
          }
          //question marks means we can use another instance object class
        //and can wrap with the ResponseEntity
          return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("id/{userName}/{myId}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId myId ,@PathVariable String userName)
    {
        journalEntryService.deleteById(myId,userName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("id/{userName}/{myId}")
    public ResponseEntity<?> updateJournalBYId(@PathVariable ObjectId myId,
                                               @RequestBody JournalEntry newEntry,
                                               @PathVariable String userName)
    {

       JournalEntry old= journalEntryService.findById(myId).orElse(null);
       if(old !=null)
       {
           old.setTitle(newEntry.getTitle()!=null && !newEntry.getTitle().equals("") ? newEntry.getTitle() : old.getTitle() );
           old.setContent(newEntry.getContent()!=null && newEntry.getContent().equals("") ? newEntry.getContent() : old.getContent());
           journalEntryService.saveEntry(old);
           return new ResponseEntity<>(old,HttpStatus.OK);
       }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
