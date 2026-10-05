import { useState, useEffect, useContext } from "react";
import api from "../../api/api";
import { AuthContext } from "../../context/AuthContext";

const BookAppointments = () => {
  const { patientId } = useContext(AuthContext);
  const [doctorId, setDoctorId] = useState("");
  const [date, setDate] = useState("");
  const [time, setTime] = useState("");
  const [appointments, setAppointments] = useState([]);
  const [doctors, setDoctors] = useState([]);

  // Fetch appointments
  useEffect(() => {
    const fetchAppointments = async () => {
      try {
        if (!patientId) return;
        const response = await api.get(
          `/api/appointments/patient/${patientId}`,
        );
        const appointmentData = response.data.data;
        setAppointments(
          Array.isArray(appointmentData) ? appointmentData : [appointmentData],
        );
      } catch (err) {
        console.error("Error fetching appointments", err);
      }
    };
    fetchAppointments();
  }, [patientId]);

  // Fetch doctors list
  useEffect(() => {
    const fetchDoctors = async () => {
      try {
        const res = await api.get("/api/doctor/all");
        setDoctors(res.data.data || res.data);
      } catch (err) {
        console.error("Error fetching doctors", err);
      }
    };
    fetchDoctors();
  }, []);

  // Book new appointment
  const bookAppointment = async () => {
    try {
      if (!doctorId || !date || !time) {
        alert("Please select a doctor, date, and time before booking.");
        return;
      }

      const res = await api.post(
        `/api/appointments/patient/${patientId}/doctor/${doctorId}`,
        { date, time },
      );

      alert("✅ Appointment booked successfully!");

      setAppointments([...appointments, res.data.data]);

      setDoctorId("");
      setDate("");
      setTime("");
    } catch (err) {
      alert(err.response?.data?.message || "Error booking appointment");
    }
  };

  return (
    <div style={styles.container}>
      <h1 style={styles.heading}>📅 Book Appointment</h1>

      {/* Booking Form */}
      <div style={styles.formCard}>
        <label style={styles.label}>Choose Doctor</label>
        <select
          value={doctorId}
          onChange={(e) => setDoctorId(e.target.value)}
          style={styles.input}
        >
          <option value="">-- Select Doctor --</option>
          {doctors.map((doc) => (
            <option key={doc.id} value={doc.id}>
              {doc.name} ({doc.specialization})
            </option>
          ))}
        </select>

        <label style={styles.label}>Date</label>
        <input
          type="date"
          value={date}
          onChange={(e) => setDate(e.target.value)}
          style={styles.input}
        />

        <label style={styles.label}>Time</label>
        <input
          type="time"
          value={time}
          onChange={(e) => setTime(e.target.value)}
          style={styles.input}
        />

        <button onClick={bookAppointment} style={styles.button}>
          Book Appointment
        </button>
      </div>

      {/* Appointment Table */}
      <h2 style={styles.subHeading}>Booked Appointments</h2>
      {appointments.length > 0 ? (
        <table style={styles.table}>
          <thead>
            <tr>
              <th style={styles.th}>ID</th>
              <th style={styles.th}>Doctor</th>
              <th style={styles.th}>Date</th>
              <th style={styles.th}>Time</th>
              <th style={styles.th}>Status</th>
            </tr>
          </thead>
          <tbody>
            {appointments.map((appt) => (
              <tr key={appt.id}>
                <td style={styles.td}>{appt.id}</td>
                <td style={styles.td}>{appt.doctor?.name || appt.doctorId}</td>
                <td style={styles.td}>{appt.date || appt.appointmentDate}</td>
                <td style={styles.td}>{appt.time || appt.appointmentTime}</td>
                <td style={styles.td}>{"BOOKED"}</td>
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

export default BookAppointments;

const styles = {
  container: {
    padding: "40px",
    fontFamily: "'Segoe UI', Tahoma, Geneva, Verdana, sans-serif",
    background: "linear-gradient(135deg, #fdfbfb 0%, #ebedee 100%)",
    minHeight: "100vh",
  },
  heading: {
    textAlign: "center",
    marginBottom: "30px",
    color: "#2c3e50",
    fontSize: "2rem",
  },
  subHeading: {
    marginTop: "40px",
    marginBottom: "20px",
    color: "#34495e",
    textAlign: "center",
  },
  formCard: {
    background: "#fff",
    padding: "20px",
    borderRadius: "10px",
    boxShadow: "0 6px 20px rgba(0,0,0,0.1)",
    maxWidth: "400px",
    margin: "0 auto",
  },
  label: {
    marginTop: "10px",
    marginBottom: "5px",
    fontWeight: "600",
    color: "#2c3e50",
  },
  input: {
    padding: "10px",
    borderRadius: "6px",
    border: "1px solid #ccc",
    marginBottom: "15px",
    width: "100%",
  },
  button: {
    padding: "12px",
    background: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
    color: "white",
    border: "none",
    borderRadius: "8px",
    cursor: "pointer",
    fontWeight: "bold",
    width: "100%",
  },
  table: {
    width: "100%",
    borderCollapse: "collapse",
    marginTop: "20px",
    boxShadow: "0 6px 16px rgba(0,0,0,0.15)",
    borderRadius: "12px",
    overflow: "hidden",
    background: "linear-gradient(135deg, #fdfbfb 0%, #ebedee 100%)",
  },
  th: {
    background: "linear-gradient(135deg, #2c3e50, #34495e)",
    color: "white",
    padding: "14px",
    textAlign: "center",
    fontWeight: "700",
    fontSize: "15px",
    border: "1px solid #444",
    letterSpacing: "0.5px",
  },
  td: {
    padding: "12px",
    border: "1px solid #ddd",
    textAlign: "center",
    fontSize: "14px",
    background: "#fafafa",
    transition: "background 0.3s ease",
  },
  rowAlt: { background: "#f0f4f8" },
  rowHover: { background: "linear-gradient(135deg, #dfe6e9, #f0f4f8)" },
  noData: {
    textAlign: "center",
    color: "#888",
    marginTop: "20px",
    fontStyle: "italic",
  },
};
