import React, { useState } from "react";
import api from "../api/api";
import { useNavigate } from "react-router-dom";

function Register() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleRegister = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError("");
    setSuccess("");
    try {
      const response = await api.post("/api/auth/register", {
        email,
        password,
      });
      setSuccess(response.data.message);
      setEmail("");
      setPassword("");
      setTimeout(() => navigate("/login"), 2000); // redirect after 2s
    } catch (error) {
      setError(error.response?.data?.message || "Registration failed");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={styles.container}>
      <div style={styles.card}>
        <h2 style={styles.title}>Create Account</h2>
        <form onSubmit={handleRegister} style={styles.form}>
          <input
            type="email"
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            style={styles.input}
            required
          />
          <div style={styles.passwordWrapper}>
            <input
              type={showPassword ? "text" : "password"}
              placeholder="Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              style={styles.input}
              required
            />
            <button
              type="button"
              onClick={() => setShowPassword(!showPassword)}
              style={styles.toggleBtn}
            >
              {showPassword ? "Hide" : "Show"}
            </button>
          </div>
          <button type="submit" style={styles.button} disabled={loading}>
            {loading ? "⏳ Registering..." : "Register"}
          </button>
        </form>
        {error && <p style={styles.errorMsg}>{error}</p>}
        {success && <p style={styles.successMsg}>{success}</p>}
        <p style={styles.text}>
          Already have an account?{" "}
          <button onClick={() => navigate("/")} style={styles.linkButton}>
            Login
          </button>
        </p>
      </div>
    </div>
  );
}
export default Register;

const styles = {
  container: {
    height: "100vh",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    background: "linear-gradient(135deg, #43cea2 0%, #185a9d 100%)",
    fontFamily: "Arial, sans-serif",
  },
  card: {
    background: "#fff",
    padding: "30px",
    borderRadius: "10px",
    boxShadow: "0 8px 20px rgba(0,0,0,0.2)",
    width: "350px",
    textAlign: "center",
  },
  title: {
    marginBottom: "20px",
    fontSize: "24px",
    fontWeight: "600",
    color: "#333",
  },
  form: {
    display: "flex",
    flexDirection: "column",
    gap: "15px",
  },
  input: {
    padding: "12px",
    border: "1px solid #ccc",
    borderRadius: "6px",
    fontSize: "14px",
    width: "80%",
  },
  passwordWrapper: {
    display: "flex",
    alignItems: "center",
    gap: "10px",
  },
  toggleBtn: {
    background: "none",
    border: "none",
    color: "#185a9d",
    cursor: "pointer",
    fontSize: "12px",
    fontWeight: "600",
  },
  button: {
    padding: "12px",
    background: "linear-gradient(135deg, #43cea2 0%, #185a9d 100%)",
    color: "#fff",
    border: "none",
    borderRadius: "6px",
    fontSize: "16px",
    fontWeight: "600",
    cursor: "pointer",
    transition: "transform 0.2s ease",
  },
  linkButton: {
    background: "none",
    border: "none",
    color: "#185a9d",
    fontWeight: "600",
    cursor: "pointer",
    textDecoration: "underline",
  },
  text: {
    marginTop: "15px",
    fontSize: "14px",
    color: "#555",
  },
  errorMsg: { color: "#e74c3c", marginTop: "10px", fontWeight: "600" },
  successMsg: { color: "#27ae60", marginTop: "10px", fontWeight: "600" },
  loading: { color: "#3498db", marginTop: "10px" },
};
