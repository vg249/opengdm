echo off
curl -i -H "Accept: application/json" -H "Content-Type: application/json"  -H "Cache-Control: no-cache, no-store, must-revalidate" -d "{\"name\":\"article\",\"scope\":\"dumb scope\"}" http://localhost:8080/gobii-web/resource/search/bycontenttype
