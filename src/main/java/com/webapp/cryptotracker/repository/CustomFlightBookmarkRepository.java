package com.webapp.cryptotracker.repository;

import com.webapp.cryptotracker.dto.BookmarkDto;
import com.webapp.cryptotracker.entity.FlightBookmark;

public interface CustomFlightBookmarkRepository {
    FlightBookmark saveBookmarkToFirebase(FlightBookmark bookmark, BookmarkDto savedBookmark);
}
