-- Mật khẩu "123456" đã được mã hóa bằng BCrypt hợp lệ (đủ 60 ký tự)
INSERT INTO users (id, username, password, enabled, role)
VALUES ('a5989e24-9b2f-48d8-a006-c87a55097bc8', 'john', 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huIiwiaWF0IjoxNzgwMTQ5Nzg2LCJleHAiOjE3ODAxNTMzODZ9.k4QdtrmDjLXJpxhAW9gEVrP8NAo00RC5Sp0sz3QU1Xo', true, 'ROLE_USER')
ON CONFLICT (username) DO NOTHING;