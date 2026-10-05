import { useEffect, useState, useContext } from "react";
import { useParams } from "react-router-dom";
import api from "../../api/api";
import { AuthContext } from "../../context/AuthContext";

const PatientDetails = () => {
  const { token } = useContext(AuthContext);
  const { appointmentId } = useParams();
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [loading, setLoading] = useState(true);
  const [patient, setPatient] = useState(null);
  const [editingId, setEditingId] = useState(null);
  const [prescriptions, setPrescriptions] = useState([]);
  const [newPrescription, setNewPrescription] = useState({
    medicines: "",
    dosage: "",
    notes: "",
  });

  useEffect(() => {
    const fetchData = async () => {
      try {
        const patientRes = await api.get(
          `/api/appointments/patients/${appointmentId}`,
        );
        setPatient(patientRes.data.data);

        const prescRes = await api.get(
          `/api/prescription/appointment/${appointmentId}`,
        );
        const data = prescRes.data.data;
        setPrescriptions(Array.isArray(data) ? data : data ? [data] : []);
      } catch (error) {
        setError("Previously not assigned any prescription");
      } finally {
        setLoading(false);
      }
    };
    if (token) fetchData();
  }, [token, appointmentId]);

  const handleAddPrescription = async () => {
    if (!newPrescription.medicines || !newPrescription.dosage) {
      setError("Medicine and dosage are required");
      return;
    }
    try {
      await api.post(`/api/prescription/appointment/${appointmentId}`, {
        ...newPrescription,
      });
      setNewPrescription({ medicines: "", dosage: "", notes: "" });
      const response = await api.get(
        `/api/prescription/appointment/${appointmentId}`,
      );
      const data = response.data.data;
      setPrescriptions(Array.isArray(data) ? data : data ? [data] : []);
      setSuccess("✅ Prescription added successfully");
    } catch (error) {
      setError(error.response?.data?.message || "Error adding prescription");
    }
  };

  const handleUpdatePrescription = async (presc) => {
    if (!presc.medicines || !presc.dosage) {
      setError("Medicine and dosage are required");
      return;
    }
    try {
      const response = await api.put(`/api/prescription/${presc.id}`, {
        medicines: presc.medicines,
        dosage: presc.dosage,
        notes: presc.notes,
      });
      setPrescriptions(
        prescriptions.map((p) => (p.id === presc.id ? response.data.data : p)),
      );
      setEditingId(null);
      setSuccess("✅ Prescription updated successfully");
    } catch (error) {
      setError(error.response?.data?.message || "Error updating prescription");
    }
  };

  const handleDeletePrescription = async (presc) => {
    try {
      if (window.confirm("Are you sure you want to delete?")) {
        await api.delete(`/api/prescription/${presc.id}`);
        setPrescriptions(prescriptions.filter((p) => p.id !== presc.id));
        setSuccess("✅ Prescription deleted successfully");
      }
    } catch (error) {
      setError(error.response?.data?.message || "Error deleting prescription");
    }
  };

  if (loading)
    return <p style={styles.loading}>⏳ Loading patient details...</p>;
  if (!patient) return <p style={styles.errorMsg}>Patient not found</p>;

  return (
    <div style={styles.container}>
      <h2 style={styles.heading}>Patient Details</h2>
      {error && <p style={styles.errorMsg}>{error}</p>}
      {success && <p style={styles.successMsg}>{success}</p>}

      <div style={styles.card}>
        <p>
          <strong>ID:</strong> {patient.id}
        </p>
        <p>
          <strong>Name:</strong> {patient.name}
        </p>
        <p>
          <strong>Email:</strong> {patient.email}
        </p>
        <p>
          <strong>Contact:</strong> {patient.contact}
        </p>
        <p>
          <strong>Medical History:</strong> {patient.medicalHistory}
        </p>
      </div>

      <h3 style={styles.subHeading}>Prescriptions</h3>
      {prescriptions.length === 0 ? (
        <p style={styles.noData}>No prescriptions yet</p>
      ) : (
        <table style={styles.table}>
          <thead>
            <tr>
              <th style={styles.th}>ID</th>
              <th style={styles.th}>Medicine</th>
              <th style={styles.th}>Dosage</th>
              <th style={styles.th}>Notes</th>
              <th style={styles.th}>Date</th>
              <th style={styles.th}>Actions</th>
            </tr>
          </thead>
          <tbody>
            {prescriptions.map((presc, index) => (
              <tr key={presc.id} style={index % 2 === 0 ? styles.rowAlt : {}}>
                <td style={styles.td}>{presc.id}</td>
                {editingId === presc.id ? (
                  <>
                    <td style={styles.td}>
                      <input
                        type="text"
                        value={presc.medicines}
                        onChange={(e) =>
                          setPrescriptions(
                            prescriptions.map((p) =>
                              p.id === presc.id
                                ? { ...p, medicines: e.target.value }
                                : p,
                            ),
                          )
                        }
                      />
                    </td>
                    <td style={styles.td}>
                      <input
                        type="text"
                        value={presc.dosage}
                        onChange={(e) =>
                          setPrescriptions(
                            prescriptions.map((p) =>
                              p.id === presc.id
                                ? { ...p, dosage: e.target.value }
                                : p,
                            ),
                          )
                        }
                      />
                    </td>
                    <td style={styles.td}>
                      <textarea
                        value={presc.notes}
                        onChange={(e) =>
                          setPrescriptions(
                            prescriptions.map((p) =>
                              p.id === presc.id
                                ? { ...p, notes: e.target.value }
                                : p,
                            ),
                          )
                        }
                      />
                    </td>
                    <td style={styles.td}>
                      {new Date(presc.date).toLocaleString("en-IN")}
                    </td>
                    <td style={styles.td}>
                      <button
                        style={styles.button}
                        onClick={() => handleUpdatePrescription(presc)}
                      >
                        Save
                      </button>
                      <button
                        style={styles.buttonSecondary}
                        onClick={() => setEditingId(null)}
                      >
                        Cancel
                      </button>
                    </td>
                  </>
                ) : (
                  <>
                    <td style={styles.td}>{presc.medicines}</td>
                    <td style={styles.td}>{presc.dosage}</td>
                    <td style={styles.td}>{presc.notes}</td>
                    <td style={styles.td}>
                      {new Date(presc.date).toLocaleString("en-IN")}
                    </td>
                    <td style={styles.td}>
                      <button
                        style={styles.button}
                        onClick={() => setEditingId(presc.id)}
                      >
                        Edit
                      </button>
                      <button
                        style={styles.buttonSecondary}
                        onClick={() => handleDeletePrescription(presc)}
                      >
                        Delete
                      </button>
                    </td>
                  </>
                )}
              </tr>
            ))}
          </tbody>
        </table>
      )}

      <h3 style={styles.subHeading}>Add New Prescription</h3>
      <div style={styles.form}>
        <input
          type="text"
          placeholder="Medicine"
          value={newPrescription.medicines}
          onChange={(e) =>
            setNewPrescription({
              ...newPrescription,
              medicines: e.target.value,
            })
          }
          style={styles.input}
        />
        <input
          type="text"
          placeholder="Dosage"
          value={newPrescription.dosage}
          onChange={(e) =>
            setNewPrescription({ ...newPrescription, dosage: e.target.value })
          }
          style={styles.input}
        />
        <textarea
          placeholder="Notes"
          value={newPrescription.notes}
          onChange={(e) =>
            setNewPrescription({ ...newPrescription, notes: e.target.value })
          }
          style={styles.textarea}
        />
        <button
          onClick={handleAddPrescription}
          style={styles.button}
          disabled={!newPrescription.medicines}
        >
          Save Prescription
        </button>
      </div>
    </div>
  );
};

export default PatientDetails;
const styles = {
  container: {
    padding: "20px",
    fontFamily: "'Segoe UI', Tahoma, Geneva, Verdana, sans-serif",
    background: "linear-gradient(135deg, #74ebd5 0%, #9face6 100%)", // ✅ gradient background
    minHeight: "100vh",
  },
  heading: {
    textAlign: "center",
    color: "#fff",
    marginBottom: "20px",
    textShadow: "1px 1px 3px rgba(0,0,0,0.3)", // ✅ subtle shadow
  },
  subHeading: {
    marginTop: "20px",
    color: "#34495e",
  },
  card: {
    background: "linear-gradient(135deg, #fdfbfb 0%, #ebedee 100%)", // ✅ soft gradient card
    padding: "20px",
    borderRadius: "12px",
    boxShadow: "0 8px 20px rgba(0,0,0,0.15)",
    maxWidth: "500px",
    margin: "0 auto",
  },
  table: {
    width: "100%",
    borderCollapse: "collapse",
    marginTop: "20px",
    boxShadow: "0 2px 8px rgba(0,0,0,0.1)",
  },
  th: {
    padding: "12px",
    textAlign: "left",
    background: "linear-gradient(135deg, #16a085 0%, #1abc9c 100%)", // ✅ gradient header
    color: "white",
  },
  td: {
    padding: "10px",
    borderBottom: "1px solid #ddd",
  },
  rowAlt: {
    backgroundColor: "#f9f9f9",
  },
  form: {
    marginTop: "20px",
    display: "flex",
    flexDirection: "column",
    gap: "10px",
    maxWidth: "400px",
    margin: "0 auto",
  },
  input: {
    padding: "10px",
    border: "1px solid #ccc",
    borderRadius: "6px",
  },
  textarea: {
    padding: "10px",
    border: "1px solid #ccc",
    borderRadius: "6px",
    minHeight: "80px",
  },
  button: {
    padding: "10px",
    background: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)", // ✅ gradient button
    color: "white",
    border: "none",
    borderRadius: "6px",
    cursor: "pointer",
    fontWeight: "600",
    transition: "transform 0.2s ease",
  },
  buttonDanger: {
    padding: "8px 12px",
    background: "linear-gradient(135deg, #e74c3c 0%, #c0392b 100%)", // ✅ gradient delete
    color: "white",
    border: "none",
    borderRadius: "6px",
    cursor: "pointer",
  },
  buttonSecondary: {
    padding: "8px 12px",
    background: "linear-gradient(135deg, #7f8c8d 0%, #95a5a6 100%)", // ✅ gradient cancel
    color: "white",
    border: "none",
    borderRadius: "6px",
    cursor: "pointer",
  },
  loading: {
    textAlign: "center",
    color: "#fff",
    marginTop: "20px",
    fontSize: "18px",
  },
  noData: {
    textAlign: "center",
    color: "#7f8c8d",
    marginTop: "10px",
  },
  errorMsg: {
    textAlign: "center",
    color: "#e74c3c",
    marginTop: "10px",
  },
  tableContainer: {
    overflowX: "auto",
    marginTop: "20px",
  },
  actionGroup: {
    display: "flex",
    gap: "8px",
  },
  successMsg: {
    textAlign: "center",
    color: "#27ae60",
    marginTop: "10px",
    fontWeight: "600",
  },
  spinner: {
    border: "4px solid #f3f3f3",
    borderTop: "4px solid #3498db",
    borderRadius: "50%",
    width: "40px",
    height: "40px",
    animation: "spin 1s linear infinite",
    margin: "20px auto",
  },
};
