package farandula

import (
	"bytes"
	"encoding/base64"
	"encoding/json"
	"fmt"
	"net/http"
)

const (
	sabreClientKey     string = "V1:5wm4lfl2h9s4wgxw:DEVCENTER:EXT"
	sabreClientSecret  string = "Y8gw3vVJ"
	sabreBaseUrl       string = "https://api.test.sabre.com"
	sabreTokenEndpoint string = "/v2/auth/token"
	sabreTokenUrl      string = sabreBaseUrl + sabreTokenEndpoint
)

type SabreGDS struct {
	client      *http.Client
	token       sabreToken
	credentials string
}

// TODO: Check auth errors to refresh token (ignore `ExpiresIn`)
// o bien cambiar `ExpiresIn` por un objeto time.Time
type sabreToken struct {
	AccessToken string `json:"access_token"`
	TokenType   string `json:"token_type"`
	ExpiresIn   int64  `json:"expires_in"`
}

func NewSabreGDS() (*SabreGDS, error) {
	sabre := &SabreGDS{client: &http.Client{}}
	sabre.generateCredentials()
	err := sabre.generateToken()
	if err != nil {
		return nil, err
	}
	return sabre, nil
}

func (sabre *SabreGDS) generateCredentials() {
	keydata := []byte(sabreClientKey)
	keystr := base64.StdEncoding.EncodeToString(keydata)

	secretdata := []byte(sabreClientSecret)
	secretstr := base64.StdEncoding.EncodeToString(secretdata)

	credentialsdata := []byte(keystr + ":" + secretstr)
	credentials := base64.StdEncoding.EncodeToString(credentialsdata)
	sabre.credentials = credentials
}

func (sabre *SabreGDS) generateToken() error {
	var token sabreToken
	data := []byte(`grant_type=client_credentials`)
	req, err := http.NewRequest("POST", sabreTokenUrl, bytes.NewBuffer(data))
	if err != nil {
		return err
	}
	req.Header.Set("Content-Type", "application/x-www-form-urlencoded")
	req.Header.Set("Authorization", "Basic "+sabre.credentials)
	resp, err := sabre.client.Do(req)
	if err != nil {
		return err
	}
	if resp.Status != "200 OK" {
		return fmt.Errorf("generating token: reponse status = %v", resp.Status)
	}
	err = json.NewDecoder(resp.Body).Decode(&token)
	if err != nil {
		return err
	}
	sabre.token = token
	return nil
}

func (sabre SabreGDS) GetAvail(q GDSQuery) (GDSResult, error) {
	return GDSResult{}, nil
}

/*
// The following is a manual GetAvail where jsonRequestExample
// corresponds to `farandula-go/data/sabre-request.json`
func (sabre SabreGDS) GetAvail(limit int, offset int) []byte {
	q := url.Values{}
	q.Add("mode", "live")
	q.Add("limit", fmt.Sprintf("%d", limit))
	q.Add("offset", fmt.Sprintf("%d", offset))
	uri := "/v3.1.0/shop/flights" + "?" + q.Encode()
	data := []byte(jsonRequestExample)
	req, err := http.NewRequest("POST", sabreBaseUrl+uri, bytes.NewBuffer(data))
	if err != nil {
		panic(err)
	}
	req.Header.Set("Content-Type", "application/json")
	req.Header.Set("Authorization", "Bearer "+sabre.token.accessToken)
	resp, err := sabre.client.Do(req)
	if err != nil {
		panic(err)
	}
	if resp.Status != "200 OK" {
		panic(resp.Status)
	}
	bs, err := ioutil.ReadAll(resp.Body)
	return bs
}
*/
