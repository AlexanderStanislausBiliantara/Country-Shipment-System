import { useEffect, useState } from 'react'
import './App.css'

interface CustomsCheck {
  id: number;
  originCountry: string;
  destCountry: string;
  originRegion: string | null;
  destRegion: string | null;
  tier: string;
  createdAt: string;
}

const API_BASE_URL = 'http://localhost:8080/api/custom-checks';

function App() {
  const [originCode, setOriginCode] = useState('');
  const [destCode, setDestCode] = useState('');
  const [checks, setChecks] = useState<CustomsCheck[]>([]);
  const [loading, setLoading] = useState(false);
  const [_error, setError] = useState('');

  const fetchChecks = async () => {
    try {
      const response = await fetch(API_BASE_URL);
      
      if (!response.ok) {
        throw new Error('Failed to fetch data.');
      }

      const data = await response.json();
      setChecks(data);

    } catch (err) {
      setError('Could not load customs checks.');
      console.log(err);
    }
  };

  useEffect(() => {
    fetchChecks();
  }, []);

  const handleSubmit = async (event: React.SubmitEvent): Promise<void> => {
    event.preventDefault();

    if (originCode.length != 2 || destCode.length != 2) {
      setError('Country codes must be 2 letters.');
      return;
    }

    for (let i = 0;i < originCode.length;i++) {
      const code = originCode.charCodeAt(i);
      const isUpperCase = code >= 65 && code <= 90;
      const isLowerCase = code >= 97 && code <= 122;

      if (!isUpperCase && !isLowerCase) {
        setError('Country codes must be uppercase or lowercase letters.');
        return;
      }
    }

    const codeRegex = /^[A-Za-z]{2}$/;
    if (!codeRegex.test(originCode) || !codeRegex.test(destCode)) {
      setError('Country codes must be 2 letters.');
      return;
    }

    setLoading(true);
    setError('');

    try {
      const response = await fetch(API_BASE_URL, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          origCode: originCode.toUpperCase(),
          destCode: destCode.toUpperCase()
        }),
      });

      if (!response.ok) {
        throw new Error('Failed to create customs check.');
      }
    } catch (err) {
        setError('Could not insert new customs check.');
        console.log(err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <div className="dashboard-container">
        <h1>Customs Check Dashboard</h1>

        <form onSubmit={handleSubmit} className="form-container">
          <div className="form-group">
            <label>Origin Code</label>
            <input
              type="text"
              value={originCode}
              onChange={(e) => setOriginCode(e.target.value)}
              maxLength={3}
              className="form-input"
              required
            />
          </div>

          <div className="form-group">
            <label>Destination Code</label>
            <input 
              type="text"
              value={destCode}
              onChange={(e) => setDestCode(e.target.value)}
              maxLength={3}
              className="form-input"
              required
            />
          </div>

          <button type="submit" disabled={loading} className="submit-btn">
            {loading ? 'Processing...' : 'Create Check'}
          </button>
        </form>

        {checks.length === 0 ? (
          <p>No customs checks found.</p>
        ) : (
          <table className="checks-table">
            <thead>
              <tr>
                <th>Origin</th>
                <th>Destination</th>
                <th>Origin Region</th>
                <th>Dest Region</th>
                <th>Tier</th>
                <th>Created At</th>
              </tr>
            </thead>
            <tbody>
              {checks.map((check, index) => (
                <tr key={check.id || index}>
                  <td>{check.originCountry}</td>
                  <td>{check.destCountry}</td>
                  <td>{check.originRegion || 'N/A'}</td>
                  <td>{check.destRegion || 'N/A'}</td>
                  <td className={check.tier === 'UNKNOWN' ? 'tier-unknown' : 'tier-known'}>
                    {check.tier}
                  </td>
                  <td className="date-text">
                    {new Date(check.createdAt).toLocaleString()}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </>
  )
}

export default App
