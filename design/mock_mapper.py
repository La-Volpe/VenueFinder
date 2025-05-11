import requests
import json

coordinates = [
    (60.169418, 24.931618),
    (60.169818, 24.932906),
    (60.170005, 24.935105),
    (60.169108, 24.936210),
    (60.168355, 24.934869),
    (60.167560, 24.932562),
    (60.168254, 24.931532),
    (60.169012, 24.930341),
    (60.170085, 24.929569),
]

base_url = "https://restaurant-api.wolt.com/v1/pages/restaurants"
results = []

for lat, lon in coordinates:
    url = f"{base_url}?lat={lat}&lon={lon}"

    try:
        response = requests.get(url)
        response.raise_for_status()
        data = response.json()
    except Exception as e:
        print(f"Error fetching data for coordinates [{lat}, {lon}]: {str(e)}")
        continue

    venues = []
    sections = data.get("sections", [])

    for section in sections:
        items = section.get("items", [])
        for item in items:
            if "venue" not in item:
                continue

            venue = item["venue"]
            image = item.get("image", {})

            venue_data = {
                "name": venue.get("name"),
                "short_description": venue.get("short_description"),
                "id": venue.get("id"),
                "image_url": image.get("url") if image else None
            }

            # Filter out None values to match your example
            venue_data = {k: v for k, v in venue_data.items() if v is not None}
            venues.append(venue_data)

            if len(venues) >= 15:
                break
        else:
            continue
        break

    results.append({
        "coords": [lat, lon],
        "venues": venues
    })

# Save to file with pretty printing
with open("mapped-response.json", "w", encoding="utf-8") as f:
    json.dump(results, f, indent=2, ensure_ascii=False)

print("Data successfully saved to mapped-response.json")