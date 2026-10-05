import { useContext, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../../api/api";
import { AuthContext } from "../../context/AuthContext";

const AdminDashBoard = () => {
  const { logout } = useContext(AuthContext);
  const [appointments, setAppointments] = useState([]);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);
  const [showAppointments, setShowAppointments] = useState(false);
  const [search, setSearch] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    const fetchAppointments = async () => {
      try {
        const response = await api.get("/api/appointments");
        setAppointments(response.data.data);
      } catch (error) {
        setError(
          error.response?.data?.message || "Error fetching appointments",
        );
      } finally {
        setLoading(false);
      }
    };
    fetchAppointments();
  }, []);

  if (loading) return <p style={styles.loading}>⏳ Loading appointments...</p>;
  if (error) return <p style={styles.errorMsg}>{error}</p>;

  const filteredAppointments = appointments.filter(
    (appt) =>
      appt.patient?.name.toLowerCase().includes(search.toLowerCase()) ||
      appt.doctor?.name.toLowerCase().includes(search.toLowerCase()),
  );

  return (
    <div style={styles.container}>
      <h2 style={styles.heading}>Admin Dashboard</h2>

      <div style={styles.controls}>
        <input
          type="text"
          placeholder="🔍 Search by patient or doctor..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          style={styles.search}
        />
        <button
          style={styles.toggleButton}
          onClick={() => setShowAppointments(!showAppointments)}
        >
          {showAppointments ? "Hide Appointments" : "Show Appointments"}
        </button>
        <button style={styles.logoutButton} onClick={logout}>
          Logout
        </button>
      </div>

      {showAppointments && (
        <div style={{ overflowX: "auto" }}>
          <table style={styles.table}>
            <thead>
              <tr>
                <th style={styles.th}>ID</th>
                <th style={styles.th}>Patient</th>
                <th style={styles.th}>Doctor</th>
                <th style={styles.th}>Date</th>
                <th style={styles.th}>Time</th>
                <th style={styles.th}>Actions</th>
              </tr>
            </thead>
            <tbody>
              {filteredAppointments.map((appt) => (
                <tr key={appt.id} style={styles.row}>
                  <td style={styles.td}>{appt.id}</td>
                  <td style={styles.td}>{appt.patient?.name}</td>
                  <td style={styles.td}>{appt.doctor?.name}</td>
                  <td style={styles.td}>{appt.date}</td>
                  <td style={styles.td}>{appt.time}</td>
                  <td style={styles.td}>
                    <button
                      style={styles.buttonAlt}
                      onClick={() => navigate(`/billing/${appt.id}`)}
                    >
                      Generate bill
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
};

export default AdminDashBoard;

const styles = {
  container: {
    padding: "40px",
    fontFamily: "'Segoe UI', Tahoma, Geneva, Verdana, sans-serif",
    background: "linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%)",
    minHeight: "100vh",
  },
  heading: {
    textAlign: "center",
    color: "#2c3e50",
    marginBottom: "30px",
  },
  controls: {
    display: "flex",
    justifyContent: "space-between",
    marginBottom: "20px",
  },
  search: {
    padding: "10px",
    border: "1px solid #ccc",
    borderRadius: "6px",
    width: "60%",
  },
  toggleButton: {
    padding: "10px 15px",
    background: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
    color: "white",
    border: "none",
    borderRadius: "6px",
    cursor: "pointer",
    fontWeight: "600",
  },
  table: {
    width: "100%",
    borderCollapse: "collapse",
    boxShadow: "0 4px 12px rgba(0,0,0,0.1)",
  },
  th: {
    textAlign: "left",
    padding: "14px",
    background: "linear-gradient(135deg, #16a085 0%, #1abc9c 100%)",
    color: "white",
    position: "sticky",
    top: 0,
  },
  td: {
    padding: "14px",
    borderBottom: "1px solid #ddd",
    backgroundColor: "#f9f9f9",
  },
  row: {
    transition: "background 0.3s",
  },
  button: {
    padding: "8px 12px",
    background: "linear-gradient(135deg, #43cea2 0%, #185a9d 100%)",
    color: "white",
    border: "none",
    borderRadius: "6px",
    cursor: "pointer",
    fontWeight: "600",
    marginRight: "8px",
  },
  buttonAlt: {
    padding: "8px 12px",
    background: "linear-gradient(135deg, #f7971e 0%, #ffd200 100%)",
    color: "white",
    border: "none",
    borderRadius: "6px",
    cursor: "pointer",
    fontWeight: "600",
  },
  loading: {
    textAlign: "center",
    color: "#3498db",
    marginTop: "20px",
    fontSize: "18px",
  },
  errorMsg: { textAlign: "center", color: "#e74c3c", marginTop: "10px" },
  logoutButton: {
    padding: "10px 15px",
    background: "linear-gradient(135deg, #e74c3c 0%, #c0392b 100%)", // red gradient
    color: "white",
    border: "none",
    borderRadius: "6px",
    cursor: "pointer",
    fontWeight: "600",
    marginLeft: "10px",
    transition: "background 0.3s ease",
  },
};
