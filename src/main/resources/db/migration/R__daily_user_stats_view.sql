CREATE OR REPLACE VIEW daily_user_stats AS
SELECT DATE(created_at) as signup_date,  COUNT(*) as new_users
FROM users
GROUP BY DATE(created_at);