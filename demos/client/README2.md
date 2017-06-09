# README

**HELP WANTED**

- Documentation
	- [ ] Describe achitecture
	- [ ] Describe how requests are sent to back-end
	- [ ] Describe JSON responses received from back-end
	- [ ] Describe how testing is done

## Architecture

## Requests to back-end

## JSON from response

## Tests

At this project we used this test: 

### Snapshot testing 

It is a form to test our UI component without writing certain test cases. Here's how it works: A snapshot is a individual state of our UI, saved in a file. We have a set of snapshots for our UI components. Once we add a new UI feature, we can generate new snapshots for the updated UI components.

### Testing action creators

Basically are functions which return plain objects. When testing action creators we want to test whether the appropriate action creator was called and also whether the right action was returned.

We can test our Front-end execute the command 

```
npm test
```