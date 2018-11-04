# Straw Free Arlington Data Processor

Simple Kotlin app to process straw free csv data files into json

## Run

`./gradlew bootRun`

## Design

1. Ingest CSV
2. Call Google Maps API
3. Produce JSON

```json
{
  "businesses": [
    {
      "name": "string",
      "starCount": 1,
      "address": "string",
      "coordinates": [0,0]
    }
  ] 
}
```