# SE333-A5-P2
# Assignment 5 – Playwright UI Testing

![Build Status](https://github.com/YOUR-USERNAME/YOUR-REPO/actions/workflows/SE333_CI.yml/badge.svg)

## Overview
This project contains automated UI testing for the DePaul University bookstore using Java, JUnit 5, and Playwright.

## Included Tests
- `playwrightTraditional` – manually developed Playwright UI test
- `playwrightLLM` – AI-assisted Playwright UI test generated with MCP-style prompting and then refined

## CI Workflow
GitHub Actions runs on pushes to the `main` branch and performs:
- Checkstyle validation
- Maven test execution
- JaCoCo report generation
- artifact upload for Checkstyle and JaCoCo results

## Status
Both Playwright test suites pass successfully and recorded test videos are included in the project.



## Reflection

For this assignment, I implemented the DePaul bookstore workflow in two different ways. First, I manually wrote my own Java playwright tests after capturing a lot of the workflow through the playwright recorder. Using the recorder tool allowed me to write the code efficiently, however, I definitely still had some difficulty as I found it neccessary to insert wait times or else I would get buggy behavior from the workflow. It took a lot of trial and error / tweaking values to get through errors. After some time, I began to have a good understanding of working around bugs while using playwright. Capturing the video and completing all steps was definitely rewarding.

The second way was done through an MCP server to allow for AI assisted UI testing. With the same goal in mind the LLM developed tests however I found many friction points as it introduced errors. Including: too broad selectors, targeting incorrect fields, targeting the wrong product, and crashing many times during video. I think that using the AI development method was helpful overall and maybe a bit faster, however, I could have deployed AI better possibly with more bounded prompting. 

In hindsight, I likely asked too much of the LLM to develope at once, and instead should have incrementally had it find success with only a chunk of steps at a time. However, it did eventually get a successful workflow and that was rewarding too. However, still not as rewarding and thoughtful as working through the problem manually in my opinion. 

I beleive that this assignemnt provided another example of how sometimes, though not always, using AI can feel like you are developing faster but that isn't the full truth. I beleive that using the AI test development was slightly faster however if I hadn't done it manually I certainly would have learned less.
