import { useEffect, useState, useContext } from "react";
import api from "../../api/api";
import { AuthContext } from "../../context/AuthContext";
import { useNavigate } from "react-router-dom";

const Appointment = () => {
  const { token, doctorId } = useContext(AuthContext);
  const [appointments, setAppointments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [search, setSearch] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    const fetchAllAppointments = async () => {
      try {
        const response = await api.get(`/api/appointments/doctor/${doctorId}`);
        setAppointments(response.data.data || []);
      } catch (error) {
        setError(
          error.response?.data?.message || "Error fetching appointments",
        );
      } finally {
        setLoading(false);
      }
    };
    if (doctorId && token) fetchAllAppointments();
  }, [doctorId, token]);

  if (loading) return <p style={styles.loading}>⏳ Loading appointments...</p>;
  if (error) return <p style={styles.errorMsg}>{error}</p>;

  // Filter appointments by patient name or date
  const filteredAppointments = appointments.filter(
    (appt) =>
      appt.patient?.name.toLowerCase().includes(search.toLowerCase()) ||
      appt.date?.toLowerCase().includes(search.toLowerCase()),
  );

  return (
    <div style={styles.container}>
      <h2 style={styles.heading}>Appointment Details</h2>

  
      <input
        type="text"
        placeholder="🔍 Search by patient or date..."
        value={search}
        onChange={(e) => setSearch(e.target.value)}
        style={styles.search}
      />

      {filteredAppointments.length > 0 ? (
        <table style={styles.table}>
          <thead>
            <tr>
              <th style={styles.th}>ID</th>
              <th style={styles.th}>Date</th>
              <th style={styles.th}>Time</th>
              <th style={styles.th}>Patient Name</th>
              <th style={styles.th}>Patient Email</th>
              <th style={styles.th}>Doctor Name</th>
              <th style={styles.th}>Specialization</th>
              <th style={styles.th}>Actions</th>
            </tr>
          </thead>
          <tbody>
            {filteredAppointments.map((appointment, index) => (
              <tr
                key={appointment.id}
                style={index % 2 === 0 ? styles.rowAlt : {}}
              >
                <td style={styles.td}>{appointment.id}</td>
                <td style={styles.td}>
                  {new Date(appointment.date).toLocaleDateString("en-IN")}
                </td>
                <td style={styles.td}>{appointment.time}</td>
                <td style={styles.td}>{appointment.patient?.name}</td>
                <td style={styles.td}>{appointment.patient?.email}</td>
                <td style={styles.td}>{appointment.doctor?.name}</td>
                <td style={styles.td}>{appointment.doctor?.specialization}</td>
                <td style={styles.td}>
                  <button
                    style={styles.button}
                    onClick={() => navigate(`/patient/${appointment.id}`)}
                  >
                    View Details
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      ) : (
        <p style={styles.noData}>No appointments found</p>
      )}
    </div>
  );
};

export default Appointment;

const styles = {
  container: { padding: "20px", fontFamily: "Segoe UI, sans-serif" },
  heading: { textAlign: "center", marginBottom: "20px", color: "#2c3e50" },
  search: {
    padding: "10px",
    marginBottom: "20px",
    width: "100%",
    borderRadius: "6px",
    border: "1px solid #ccc",
  },
  table: {
    width: "100%",
    borderCollapse: "collapse",
    boxShadow: "0 4px 12px rgba(0,0,0,0.1)",
  },
  th: {
    backgroundColor: "#16a085",
    color: "white",
    padding: "12px",
    textAlign: "left",
  },
  td: { padding: "12px", borderBottom: "1px solid #ddd" },
  rowAlt: { backgroundColor: "#f9f9f9" },
  button: {
    padding: "6px 12px",
    background: "linear-gradient(135deg, #43cea2 0%, #185a9d 100%)",
    color: "white",
    border: "none",
    borderRadius: "6px",
    cursor: "pointer",
    fontWeight: "600",
  },
  loading: { textAlign: "center", color: "#3498db", marginTop: "20px" },
  errorMsg: { textAlign: "center", color: "#e74c3c", marginTop: "20px" },
  noData: { textAlign: "center", color: "#7f8c8d", marginTop: "20px" },
};
