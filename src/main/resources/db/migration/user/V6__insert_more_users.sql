-- admin user với role ROLE_ADMIN
INSERT INTO users (id, username, password, enabled, role)
VALUES ('a5989e24-9b2f-48d8-d006-c87a55097bc8','admin', 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huIiwiaWF0IjoxNzgwMTQ5Nzg2LCJleHAiOjE3ODAxNTMzODZ9.k4QdtrmDjLXJpxhAW9gEVrP8NAo00RC5Sp0sz3QU1Xo', true, 'ROLE_ADMIN')
ON CONFLICT (username) DO NOTHING;

-- user với authority đặc biệt (có thể lưu role dưới dạng authority)
INSERT INTO users (id, username, password, enabled, role)
VALUES ('a5989e24-9b2f-48d8-b006-c87a55097bc8','doctor', 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huIiwiaWF0IjoxNzgwMTQ5Nzg2LCJleHAiOjE3ODAxNTMzODZ9.k4QdtrmDjLXJpxhAW9gEVrP8NAo00RC5Sp0sz3QU1Xo', true, 'patient:read,ROLE_DOCTOR')
ON CONFLICT (username) DO NOTHING;