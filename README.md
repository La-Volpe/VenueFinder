# Mobile app assignment

Please see the [NOTES.md](https://github.com/La-Volpe/wolt-android-task/blob/my-implementation-v2/NOTES.md) and [QA Notes](https://github.com/La-Volpe/wolt-android-task/blob/my-implementation-v2/QA-CHECKLIST.md) for more on the project.

## Concept

Helping a user walking around the Helsinki city center looking for a place to eat using Wolt's search.

## Task

### Description

Create an application that performs the following functions:

1. Continuously display a list of up to 15 venues near the user’s current location. If the API returns more than 15 venues display only the first 15 from the list.
2. The user’s location should update every 10 seconds to the next coordinate in the provided input list (in this document). After the last location in the list, the app should loop back to the first location and repeat the sequence.
3. The venue list should automatically refresh to reflect the new location with a smooth transition animation to enhance visual appeal.
4. Each venue should have a “Favourite” action that toggles (true/false) and changes the icon depending on the state. The app must remember these favorite states and reapply them to venues that appear again even after the app is restarted.

### Location Update Timeline Explanation

| Time passed after opening the app | Current location |
|-----------------------------------|------------------|
| 0 seconds                         | `locations[0]`   |
| 10 seconds                        | `locations[1]`   |
| 20 seconds                        | `locations[2]`   |
| ...                               | ...              |
| (10 * locationsCount) seconds     | `locations[0]` (looped) |

- **0 seconds**: Display venues near `locations[0]`.
- **10 seconds**: Update display to venues near `locations[1]`.
- **20 seconds**: Update display to venues near `locations[2]`.
- Continue updating every 10 seconds until the end of the list is reached, then loop back to `locations[0]` and repeat.

## API Endpoint

`GET https://restaurant-api.wolt.com/v1/pages/restaurants?lat=60.170187&lon=24.930599`

### Important fields in response (JSON)

| Path / Key                                | Meaning                           |
|-------------------------------------------|-----------------------------------|
| `sections -> items -> venue -> id`        | Unique ID of the venue            |
| `sections -> items -> venue -> name`      | Name of the venue                 |
| `sections -> items -> venue -> short_description` | Description of the venue       |
| `sections -> items -> image -> url`       | Image URL for the venue           |

### Coordinates (latitude, longitude)

- `60.169418, 24.931618`
- `60.169818, 24.932906`
- `60.170005, 24.935105`
- `60.169108, 24.936210`
- `60.168355, 24.934869`
- `60.167560, 24.932562`
- `60.168254, 24.931532`
- `60.169012, 24.930341`
- `60.170085, 24.929569`
