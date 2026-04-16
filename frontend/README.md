# Frontend (Next.js)

Enterprise-style frontend for the News backend API.

## Stack

- Next.js (App Router)
- React + TypeScript
- Tailwind CSS

## Setup

1. Copy env template:

```bash
cp .env.local.example .env.local
```

2. Install dependencies:

```bash
npm install
```

3. Start development server:

```bash
npm run dev
```

Frontend runs on `http://localhost:3000` and expects backend API at `NEXT_PUBLIC_API_URL` (default `http://localhost:8080`).

## Scripts

- `npm run dev`
- `npm run build`
- `npm run start`
- `npm run lint`

## Routes

- `/` Home (NY Times top stories)
- `/science` Science stories
- `/articles/[id]` Article detail
- `/articles/new` Create article
- `/dashboard` Article management
- `/users` User management

## Docker

Build and run:

```bash
docker build -t news-frontend .
docker run --rm -p 3000:3000 --env NEXT_PUBLIC_API_URL=http://localhost:8080 news-frontend
```
