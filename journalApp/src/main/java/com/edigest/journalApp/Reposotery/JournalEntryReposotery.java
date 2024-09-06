package com.edigest.journalApp.Reposotery;

import com.edigest.journalApp.entity.JournalEntry;
import org.bson.types.ObjectId;
import org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JournalEntryReposotery  extends MongoRepository<JournalEntry, ObjectId>
{



}
