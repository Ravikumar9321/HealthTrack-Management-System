// src/pages/Login.js
import React, { useState, useContext } from "react";
import api from "../api/api"; 
import { AuthContext } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";

export default function Login() {
  const { login } = useContext(AuthContext);
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const handleLogin = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError("");
    try {
      const res = await api.post("/api/auth/login", { email, password });
      login(res.data.token, res.data.role, res.data.doctorId, res.data.patientId);
      navigate(res.data.defaultLandingPage);
    } catch (error) {
      setError(error.response?.data?.message || "Login failed, maybe not registered");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={styles.container}>
      <div style={styles.card}>
        <h2 style={styles.title}>Login</h2>
        <form onSubmit={handleLogin} style={styles.form}>
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
            {loading ? "⏳ Logging in..." : "Login"}
          </button>
        </form>
        {error && <p style={styles.errorMsg}>{error}</p>}
        <p style={styles.text}>
          Don’t have an account?{" "}
          <button
            onClick={() => navigate("/register")}
            style={styles.linkButton}
          >
            Register
          </button>
        </p>
      </div>
    </div>
  );
}

const styles = {
  container: {
    height: "100vh",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    background: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
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
    color: "#764ba2",
    cursor: "pointer",
    fontSize: "12px",
    fontWeight: "600",
  },
  button: {
    padding: "12px",
    background: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
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
    color: "#667eea",
    fontWeight: "600",
    cursor: "pointer",
    textDecoration: "underline",
  },
  text: {
    marginTop: "15px",
    fontSize: "14px",
    color: "#555",
  },
  errorMsg: {
    marginTop: "10px",
    color: "#e74c3c",
    fontWeight: "600",
  },
};
