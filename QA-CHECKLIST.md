# QA Checklist for Venues App

## 1. Venue List
- [ ] App successfully fetches venues based on the mocked location.
- [ ] Venues display correct fields (name, short description, image).
- [ ] Only up to 15 venues are shown at once.
- [ ] Network failure shows an appropriate error message or fallback UI.

## 2. Favorites Functionality
- [ ] Tapping favorite button toggles favorite state correctly.
- [ ] Favorite state persists after app restart.
- [ ] Toggling favorites updates the UI immediately.
- [ ] No duplicate or unexpected behavior on favoriting/unfavoriting.

## 3. Location Handling
- [ ] App listens to location updates correctly.
- [ ] App reloads venues when location changes.

## 4. UI / UX Behavior
- [ ] Venue items are rendered correctly using Compose components.
- [ ] App gracefully handles empty venue lists.
- [ ] Images load correctly without errors.
- [ ] App does not crash on no network at launch.

## 5. Testing Coverage Check
- [ ] All unit tests pass (`./gradlew test`).
- [ ] Instrumentation tests pass if present (`./gradlew connectedAndroidTest`).
- [ ] Key features (favorites, venue retrieval) have test coverage.

## 6. Performance
- [ ] App cold start time is within ~2 seconds.
- [ ] Scrolling through the venues list is smooth without frame drops.

## 7. Other QA Scenarios
- [ ] Favoriting all displayed venues does not cause crashes or performance issues.
- [ ] App handles pulling internet connection while loading venues gracefully.
- [ ] App handles unexpected API responses without crashing.

