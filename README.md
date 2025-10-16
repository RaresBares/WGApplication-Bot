# WG Application Bot

A personal automation tool I developed while searching for shared flats in Zürich.  
Finding a room through platforms like **FlatFox**, **WGZimmer**, and others proved extremely time-consuming and inefficient without automation — so I built a bot to handle it.

---

## Overview

The WG Application Bot automates browsing and applying for available rooms on major Swiss housing platforms.  
It parses listings, filters them by custom criteria, and automatically sends personalized applications based on a predefined message template.

---

## Motivation

During my stay in **Zürich**, I realized that applying manually to each listing was impractical — most rooms vanished within hours.  
This bot was designed to streamline the process and maximize response rates by maintaining constant activity across multiple housing portals.

---

## Supported Platforms

- [FlatFox](https://flatfox.ch)
- [WGZimmer](https://www.wgzimmer.ch)
- [Homegate](https://www.homegate.ch) *(planned)*
- [Immoscout24](https://www.immoscout24.ch) *(planned)*

---

## Core Features

- Automated login and session handling  
- Search filters (price range, location, duration, furnished/unfurnished)  
- Scraping of new listings in real time  
- Automated message submission with configurable text templates  
- Logging of sent applications and responses  
- Adjustable delay/randomization to simulate human behavior

---

## Tech Stack

- **Python 3.x**
- **Selenium / Playwright**
- **Requests / BeautifulSoup**
- **SQLite** (for storing applications and status)
- Optional **Telegram integration** for live notifications

---

## Usage

1. Configure login credentials and search filters in `config.json`
2. Run the bot:
   ```bash
   python bot.py
