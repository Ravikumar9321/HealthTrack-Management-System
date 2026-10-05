import { useContext, useEffect, useState } from "react";
import { AuthContext } from "../../context/AuthContext";
import api from "../../api/api";
import { useNavigate } from "react-router-dom";

const PatientDashboard = () => {
  const { patientId, token,logout } = useContext(AuthContext);
  const [patientInfo, setPatientInfo] = useState(null);
  const [editing, setEditing] = useState(false);
  const [appointments, setAppointments] = useState([]);
  const [prescription, setPrescription] = useState(null);
  const [billing, setBilling] = useState(null);
  const [selectedAppt, setSelectedAppt] = useState(null);
  const [showModal, setShowModal] = useState(false);
  const [originalPatientInfo, setOriginalPatientInfo] = useState(null);
  const navigate=useNavigate();


const startEditing = () => {
  setOriginalPatientInfo(patientInfo); 
  setEditing(true);
};


const cancelEditing = () => {
  setPatientInfo(originalPatientInfo); 
  setEditing(false);
};

  
  useEffect(() => {
    const fetchAppointments = async () => {
      try {
        if (!patientId) return;
        const response = await api.get(`/api/appointments/patient/${patientId}`);
        const appointmentData = response.data.data;
              setOriginalPatientInfo(response.data.data || response.data); 
        setAppointments(Array.isArray(appointmentData) ? appointmentData : [appointmentData]);
      } catch (err) {
        console.error("Error fetching appointments", err);
      }
    };
    if (patientId && token) fetchAppointments();
  }, [patientId, token]);

  // Fetch patient info
  useEffect(() => {
    const fetchPatientInfo = async () => {
      try {
        const res = await api.get(`/api/patient/${patientId}`);
        setPatientInfo(res.data.data || res.data);
      } catch (error) {
        alert(error.response?.data.message);
      }
    };
    if (patientId && token) fetchPatientInfo();
  }, [patientId, token]);

  // Fetch prescription
  const fetchPrescription = async (apptId) => {
    try {
      const res = await api.get(`/api/prescription/appointment/${apptId}`);
      setPrescription(res.data.data || null);
    } catch {
      setPrescription(null);
    }
  };

  // Fetch billing
  const fetchBilling = async (apptId) => {
    try {
      const res = await api.get(`/api/billing/appointment/${apptId}`);
      setBilling(res.data.data || null);
    } catch {
      setBilling(null);
    }
  };

  // Update patient info
  const handlePatientUpdate = async () => {
    try {
      await api.put(`/api/patient/${patientId}`, patientInfo);
      setEditing(false);
      alert("Profile updated successfully");
    } catch (error) {
      alert(error.response?.data.message);
    }
  };

  // Handle view details
  const handleViewDetails = async (apptId) => {
    setSelectedAppt(apptId);
    setPrescription(null);
    setBilling(null);
    await fetchPrescription(apptId);
    await fetchBilling(apptId);
    setShowModal(true);
  };

  return (
    <div style={styles.container}>
      <h1 style={styles.heading}>🏥 Patient Dashboard</h1>

      {/* Patient Info Card */}
      {patientInfo && (
        <div style={styles.card}>
          <h2 style={styles.subHeading}>Patient Information</h2>
          <table style={styles.table}>
            <tbody>
              <tr>
                <th style={styles.th}>Patient ID</th>
                <td style={styles.td}>{patientInfo.id}</td>
              </tr>
              <tr>
                <th style={styles.th}>Name</th>
                <td style={styles.td}>
                  {editing ? (
                    <input
                      type="text"
                      value={patientInfo.name}
                      onChange={(e) => setPatientInfo({ ...patientInfo, name: e.target.value })}
                      style={styles.input}
                    />
                  ) : patientInfo.name}
                </td>
              </tr>
              <tr>
                <th style={styles.th}>Email</th>
                <td style={styles.td}>
                  {editing ? (
                    <input
                      type="text"
                      value={patientInfo.email}
                      onChange={(e) => setPatientInfo({ ...patientInfo, email: e.target.value })}
                      style={styles.input}
                    />
                  ) : patientInfo.email}
                </td>
              </tr>
              <tr>
                <th style={styles.th}>Gender</th>
                <td style={styles.td}>
                  {editing ? (
                    <input
                      type="text"
                      value={patientInfo.gender}
                      onChange={(e) => setPatientInfo({ ...patientInfo, gender: e.target.value })}
                      style={styles.input}
                    />
                  ) : patientInfo.gender}
                </td>
              </tr>
              <tr>
                <th style={styles.th}>Phone</th>
                <td style={styles.td}>
                  {editing ? (
                    <input
                      type="text"
                      value={patientInfo.contact}
                      onChange={(e) => setPatientInfo({ ...patientInfo, contact: e.target.value })}
                      style={styles.input}
                    />
                  ) : patientInfo.contact}
                </td>
              </tr>
            </tbody>
          </table>
         {editing ? (
  <>
    <button style={styles.button} onClick={handlePatientUpdate}>Save</button>
    <button style={styles.buttonSecondary} onClick={cancelEditing}>Cancel</button>
  </>
) : (
  <button style={styles.button} onClick={startEditing}>Edit Profile</button>
)}
  <button style={styles.button} onClick={()=>navigate(`/bookAppointment`)}>Book Appointment</button>
  <button style={styles.logoutButton} onClick={logout}>Logout</button>

        </div>
      )}

      {/* Appointments Table */}
      <div style={styles.card}>
        <h2 style={styles.subHeading}>Appointments</h2>
        {appointments.length > 0 ? (
          <table style={styles.table}>
            <thead>
              <tr>
                <th style={styles.th}>Appointment ID</th>
                <th style={styles.th}>Doctor Name</th>
                <th style={styles.th}>Time</th>
                <th style={styles.th}>Date</th>
                <th style={styles.th}>Actions</th>
              </tr>
            </thead>
            <tbody>
              {appointments.map((appt) => (
                <tr key={appt.id} style={{ cursor: "pointer" }}>
                  <td style={styles.td}>{appt.id}</td>
                  <td style={styles.td}>{appt?.doctor?.name}</td>
                  <td style={styles.td}>{appt.time}</td>
                  <td style={styles.td}>{appt.date}</td>
                  <td style={styles.td}>
                    <button style={styles.button} onClick={() => handleViewDetails(appt.id)}>
                      View Details
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        ) : <p style={styles.noData}>No appointment records found</p>}
      </div>

      {/* Modal */}
      {showModal && (
        <div style={styles.modalOverlay}>
          <div style={styles.modal}>
            <h2>Details for Appointment #{selectedAppt}</h2>

            <h3>Prescription</h3>
            {prescription ? (
              <table style={styles.table}>
                <tbody>
                  <tr><th style={styles.th}>Medicine</th><td style={styles.td}>{prescription.medicines}</td></tr>
                  <tr><th style={styles.th}>Dosage</th><td style={styles.td}>{prescription.dosage}</td></tr>
                  <tr><th style={styles.th}>Notes</th><td style={styles.td}>{prescription.notes}</td></tr>
                </tbody>
              </table>
            ) : <p style={styles.noData}>No prescription found</p>}

            <h3>Billing</h3>
            {billing ? (
              <table style={styles.table}>
                <tbody>
                  <tr><th style={styles.th}>Amount</th><td style={styles.td}>₹{billing.amount}</td></tr>
                  <tr><th style={styles.th}>Status</th><td style={styles.td}>{billing.status}</td></tr>
                </tbody>
              </table>
            ) : <p style={styles.noData}>No billing record found</p>}

            <button onClick={() => setShowModal(false)} style={styles.closeBtn}>Close</button>
          </div>
        </div>
      )}
    </div>
  );
};

export default PatientDashboard;
const styles = {
  container: {
    padding: "30px",
    fontFamily: "Segoe UI, sans-serif",
    background: "linear-gradient(135deg, #6dd5ed 0%, #2193b0 100%)",
    minHeight: "100vh",
  },
  heading: {
    textAlign: "center",
    color: "#2c3e50",
    marginBottom: "30px",
    fontSize: "32px",
    fontWeight: "700",
    textShadow: "2px 2px 6px rgba(0,0,0,0.2)",
  },
  card: {
    background: "linear-gradient(135deg, #ffffff 0%, #f3f9ff 100%)",
    padding: "25px",
    borderRadius: "14px",
    boxShadow: "0 10px 25px rgba(0,0,0,0.15)",
    marginBottom: "30px",
    transition: "transform 0.3s ease, box-shadow 0.3s ease",
  },
  subHeading: {
    color: "#34495e",
    marginBottom: "15px",
    fontSize: "22px",
    fontWeight: "600",
    borderBottom: "3px solid #6dd5ed",
    paddingBottom: "6px",
  },
  table: {
    width: "100%",
    borderCollapse: "collapse",
    marginTop: "15px",
    borderRadius: "10px",
    overflow: "hidden",
    boxShadow: "0 6px 16px rgba(0,0,0,0.08)",
  },
  th: {
    background: "linear-gradient(135deg, #2c3e50, #4b79a1)",
    color: "white",
    padding: "14px",
    textAlign: "center",
    fontWeight: "600",
    fontSize: "15px",
    letterSpacing: "0.6px",
  },
  td: {
    padding: "14px",
    borderBottom: "1px solid #ddd",
    textAlign: "center",
    fontSize: "15px",
    background: "linear-gradient(135deg, #ffffff, #f7f9fc)",
    transition: "background 0.3s ease",
  },
  rowHover: {
    background: "#eaf6ff",
  },
  input: {
    padding: "10px",
    borderRadius: "8px",
    border: "1px solid #ccc",
    width: "90%",
    fontSize: "14px",
    outline: "none",
    transition: "border 0.3s ease, box-shadow 0.3s ease",
  },
  button: {
    padding: "12px 24px",
    borderRadius: "10px",
    background: "linear-gradient(135deg, #2c3e50, #34495e)",
    color: "white",
    border: "none",
    cursor: "pointer",
    marginRight: "12px",
    fontWeight: "600",
    transition: "background 0.3s ease, transform 0.2s ease",
  },
  buttonSecondary: {
    padding: "12px 24px",
    borderRadius: "10px",
    background: "linear-gradient(135deg, #95a5a6, #7f8c8d)",
    color: "white",
    border: "none",
    cursor: "pointer",
    fontWeight: "600",
  },
  buttonDanger: {
    padding: "12px 24px",
    borderRadius: "10px",
    background: "linear-gradient(135deg, #e74c3c, #c0392b)",
    color: "white",
    border: "none",
    cursor: "pointer",
    fontWeight: "600",
  },
  noData: {
    textAlign: "center",
    color: "#888",
    fontStyle: "italic",
    marginTop: "12px",
  },
  modalOverlay: {
    position: "fixed",
    top: 0,
    left: 0,
    width: "100%",
    height: "100%",
    background: "rgba(0,0,0,0.6)",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    backdropFilter: "blur(6px)",
  },
  modal: {
    background: "linear-gradient(135deg, #ffffff, #f0f4f8)",
    padding: "30px",
    borderRadius: "14px",
    width: "600px",
    maxHeight: "80vh",
    overflowY: "auto",
    boxShadow: "0 12px 35px rgba(0,0,0,0.25)",
    animation: "fadeIn 0.4s ease",
  },
  closeBtn: {
    marginTop: "20px",
    padding: "12px 24px",
    background: "linear-gradient(135deg, #e74c3c, #c0392b)",
    color: "white",
    border: "none",
    borderRadius: "8px",
    cursor: "pointer",
    fontWeight: "600",
    transition: "background 0.3s ease",
  },
  logoutButton: {
  padding: "10px 15px",
  background: "linear-gradient(135deg, #e74c3c 0%, #885b56 100%)", 
  color: "white",
  border: "none",
  borderRadius: "6px",
  cursor: "pointer",
  fontWeight: "600",
  marginLeft: "10px",
  transition: "background 0.3s ease",
}

};
