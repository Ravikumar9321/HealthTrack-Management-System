import { useContext, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../../api/api";
import { AuthContext } from "../../context/AuthContext";

const DoctorProfile = () => {
  const { doctorId, logout } = useContext(AuthContext);
  const [doctor, setDoctor] = useState(null);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [editing, setEditing] = useState(false);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchDoctor = async () => {
      try {
        const response = await api.get(`/api/doctor/${doctorId}`);
        setDoctor(response.data.data);
      } catch (error) {
        setError(error.response?.data?.message || "Error fetching doctor profile");
      } finally {
        setLoading(false);
      }
    };
    fetchDoctor();
  }, [doctorId]);

  const handleUpdate = async () => {
    if (!doctor.name || !doctor.specialization || !doctor.schedule) {
      setError("All fields must be filled before saving");
      return;
    }
    try {
      await api.put(`/api/doctor/${doctorId}`, doctor);
      setEditing(false);
      setSuccess("✅ Profile updated successfully");
    } catch (error) {
      setError(error.response?.data?.message || "Error updating profile");
    }
  };

  if (loading) return <p style={styles.loading}>⏳ Loading profile...</p>;
  if (!doctor) return <p style={styles.errorMsg}>Doctor not found</p>;

  return (
    <div style={styles.container}>
      <h2 style={styles.heading}>Doctor Dashboard</h2>
      {error && <p style={styles.errorMsg}>{error}</p>}
      {success && <p style={styles.successMsg}>{success}</p>}

      <table style={styles.table}>
        <tbody>
          <tr>
            <th style={styles.th}>Name</th>
            <td style={styles.td}>
              {editing ? (
                <input
                  type="text"
                  value={doctor.name}
                  onChange={(e) => setDoctor({ ...doctor, name: e.target.value })}
                  style={styles.input}
                />
              ) : (
                doctor.name
              )}
            </td>
          </tr>
          <tr>
            <th style={styles.th}>Email</th>
            <td style={styles.td}>{doctor.email}</td>
          </tr>
          <tr>
            <th style={styles.th}>Specialization</th>
            <td style={styles.td}>
              {editing ? (
                <input
                  type="text"
                  value={doctor.specialization}
                  onChange={(e) =>
                    setDoctor({ ...doctor, specialization: e.target.value })
                  }
                  style={styles.input}
                />
              ) : (
                doctor.specialization
              )}
            </td>
          </tr>
          <tr>
            <th style={styles.th}>Schedule</th>
            <td style={styles.td}>
              {editing ? (
                <input
                  type="text"
                  value={doctor.schedule}
                  onChange={(e) =>
                    setDoctor({ ...doctor, schedule: e.target.value })
                  }
                  style={styles.input}
                />
              ) : (
                doctor.schedule
              )}
            </td>
          </tr>
        </tbody>
      </table>

      <div style={styles.actionGroup}>
        {editing ? (
          <>
            <button style={styles.button} onClick={handleUpdate}>Save</button>
            <button style={styles.buttonSecondary} onClick={() => setEditing(false)}>Cancel</button>
          </>
        ) : (
          <button style={styles.button} onClick={() => setEditing(true)}>Edit Profile</button>
        )}

        <button style={styles.buttonAlt} onClick={() => navigate("/appointments")}>
          View Appointment Details
        </button>

        {/* ✅ Logout with redirect */}
        <button
          style={styles.logoutButton}
          onClick={() => {
            logout();
            navigate("/login");
          }}
        >
          Logout
        </button>
      </div>
    </div>
  );
};

export default DoctorProfile;

const styles = {
  container: {
    padding: "40px",
    fontFamily: "'Segoe UI', Tahoma, Geneva, Verdana, sans-serif",
    background: "linear-gradient(135deg, #74ebd5 0%, #9face6 100%)",
    minHeight: "100vh",
  },
  heading: {
    textAlign: "center",
    color: "#fff",
    marginBottom: "30px",
    textShadow: "1px 1px 3px rgba(0,0,0,0.3)",
  },
  table: {
    width: "100%",
    borderCollapse: "collapse",
    marginBottom: "20px",
    boxShadow: "0 4px 12px rgba(0,0,0,0.1)",
  },
  th: {
    textAlign: "left",
    padding: "14px",
    background: "linear-gradient(135deg, #16a085 0%, #1abc9c 100%)",
    color: "white",
    width: "25%",
  },
  td: {
    padding: "14px",
    borderBottom: "1px solid #ddd",
    backgroundColor: "#f9f9f9",
  },
  input: {
    padding: "10px",
    border: "1px solid #ccc",
    borderRadius: "6px",
    width: "100%",
  },
  button: {
    padding: "12px 18px",
    background: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
    color: "white",
    border: "none",
    borderRadius: "6px",
    cursor: "pointer",
    fontWeight: "600",
    marginRight: "10px",
  },
  buttonSecondary: {
    padding: "12px 18px",
    background: "linear-gradient(135deg, #7f8c8d 0%, #95a5a6 100%)",
    color: "white",
    border: "none",
    borderRadius: "6px",
    cursor: "pointer",
  },
  buttonAlt: {
    padding: "12px 18px",
    background: "linear-gradient(135deg, #ff9966 0%, #ff5e62 100%)",
    color: "white",
    border: "none",
    borderRadius: "6px",
    cursor: "pointer",
    fontWeight: "600",
  },
  actionGroup: {
    display: "flex",
    gap: "10px",
    marginTop: "20px",
  },
  loading: {
    textAlign: "center",
    color: "#3498db",
    marginTop: "20px",
    fontSize: "18px",
  },
  errorMsg: { textAlign: "center", color: "#e74c3c", marginTop: "10px" },
  successMsg: {
    textAlign: "center",
    color: "#27ae60",
    marginTop: "10px",
    fontWeight: "600",
  },
  logoutButton: {
    padding: "10px 20px",
    background: "linear-gradient(135deg, #e74c3c, #c0392b)", // red gradient
    color: "white",
    border: "none",
    borderRadius: "6px",
    cursor: "pointer",
    fontWeight: "600",
    marginLeft: "10px",
    transition: "background 0.3s ease",
  },
};
