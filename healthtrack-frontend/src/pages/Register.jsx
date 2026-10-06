import React, { useState } from "react";
import api from "../api/api";
import { useNavigate } from "react-router-dom";

function Register() {
  const [role, setRole] = useState(""); // doctor or patient
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    password: "",
    specialization: "",
    schedule: "",
    gender: "",
    contact: "",
    medicalHistory: ""
  });
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleRegister = async () => {
    if (!role) {
      setError("Please select Doctor or Patient");
      return;
    }
    setLoading(true);
    setError("");
    setSuccess("");
    try {
      const endpoint = role === "doctor" ? "doctor-register" : "patient-register";
      const response = await api.post(`/api/auth/${endpoint}`, formData);
      setSuccess(response.data.message || "Registered successfully");
      setTimeout(() => navigate("/"), 2000);
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

        {/* Role Selection */}
        <div style={styles.roleButtons}>
          <button
            type="button"
            style={role === "doctor" ? styles.activeButton : styles.button}
            onClick={() => setRole("doctor")}
          >
            Register Doctor
          </button>
          <button
            type="button"
            style={role === "patient" ? styles.activeButton : styles.button}
            onClick={() => setRole("patient")}
          >
            Register Patient
          </button>
        </div>

        {/* Common Fields */}
        <input
          type="text"
          name="name"
          placeholder="Name"
          value={formData.name}
          onChange={handleChange}
          style={styles.input}
          required
        />
        <input
          type="email"
          name="email"
          placeholder="Email"
          value={formData.email}
          onChange={handleChange}
          style={styles.input}
          required
        />
        <input
          type="password"
          name="password"
          placeholder="Password"
          value={formData.password}
          onChange={handleChange}
          style={styles.input}
          required
        />

      
        {role === "doctor" && (
          <>
            <input
              type="text"
              name="specialization"
              placeholder="Specialization"
              value={formData.specialization}
              onChange={handleChange}
              style={styles.input}
              required
            />
            <input
              type="text"
              name="schedule"
              placeholder="Schedule (e.g. Mon-Fri 09:00-17:00)"
              value={formData.schedule}
              onChange={handleChange}
              style={styles.input}
              required
            />
          </>
        )}

        {/* Patient-specific fields */}
        {role === "patient" && (
          <>
            <input
              type="text"
              name="gender"
              placeholder="Gender"
              value={formData.gender}
              onChange={handleChange}
              style={styles.input}
            />
            <input
              type="text"
              name="contact"
              placeholder="Contact (10 digits)"
              value={formData.contact}
              onChange={handleChange}
              style={styles.input}
              required
            />
            <textarea
              name="medicalHistory"
              placeholder="Medical History"
              value={formData.medicalHistory}
              onChange={handleChange}
              style={styles.input}
            />
          </>
        )}

        <button
          type="button"
          style={styles.button}
          disabled={loading}
          onClick={handleRegister}
        >
          {loading ? "⏳ Registering..." : "Register"}
        </button>

        {error && <p style={styles.errorMsg}>{error}</p>}
        {success && <p style={styles.successMsg}>{success}</p>}
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
