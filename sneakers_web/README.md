# SneakrScrapeAPI

## Overview

SneakrScrapeAPI is a robust python backend API project built using Django REST Framework. Its primary objective is to scrape and manage data about sneakers, providing a comprehensive platform for searching, creating, reading, updating, and deleting (CRUD) sneakers data. The project leverages SimpleJWT for authentication and authorization, ensuring secure and efficient management of user access.

## Project Aim

SneakrScrapeAPI aims to scrape and manage data about sneakers, providing a comprehensive platform for searching, creating, reading, updating, and deleting (CRUD) sneakers data.

## Key Features

- **Scraping and Data Management:** Utilizes requests and BeautifulSoup for scraping and parsing sneakers data from various websites, with threads for asynchronous requests and pandas for data manipulation and handling.

- **Search Functionality:** Enables searching by model and category, making it easy to find specific sneakers.

- **CRUD Operations:** Provides a complete set of CRUD operations for managing sneakers data, allowing for seamless data manipulation.

- **Authentication and Authorization:** Employs SimpleJWT for secure authentication and authorization, ensuring only authorized users can access and modify data. In addition to implementing a custom class and middleware for handling authentication process, providing a flexible and customizable authentication flow.

## Packages and Tools

This project utilizes a range of packages and tools, including requests, BeautifulSoup, threads, pandas, json, SimpleJWT, and Django REST Framework, to efficiently scrape, manage, and provide access to sneakers data.

## Future Work

- **Additional Websites:** Integrate more sneakers websites into the scraper to expand the dataset.
- **Enhanced Sneakers Details:** Add more details about sneakers, such as reviews, to provide a more comprehensive dataset.
- **Improved Search Functionality:** Enhance search functionality to include filters and sorting options.
- **Data Visualization:** Implement data visualization tools to provide insights into sneakers data trends and patterns.
