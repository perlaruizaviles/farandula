package farandula

import (
	"fmt"
	"io"
	"io/ioutil"
	"net/http"
	"net/url"
	"text/template"

	"github.com/tidwall/gjson"
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
	template    *template.Template
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
	err = sabre.generateSabreRequestTemplate()
	if err != nil {
		return nil, err
	}
	return sabre, nil
}

func (sabre *SabreGDS) request(method, requrl string, body io.Reader) (*http.Response, error) {
	req, err := http.NewRequest(method, requrl, body)
	if err != nil {
		return nil, err
	}
	req.Header.Set("Content-Type", "application/json")
	req.Header.Set("Authorization", "Bearer "+sabre.token.AccessToken)

	res, err := sabre.client.Do(req)

	if err != nil {
		return nil, err
	}
	if res.Status != "200 OK" {
		return nil, fmt.Errorf("Status %v", res.Status)
	}
	return res, nil
}

func (sabre *SabreGDS) requestWithParams(method, requrl string, params map[string]string, body io.Reader) (*http.Response, error) {
	q := url.Values{}
	for k, v := range params {
		q.Add(k, v)
	}
	return sabre.request(method, requrl+"?"+q.Encode(), body)
}

func (sabre SabreGDS) GetAvail(q GDSQuery) (GDSResult, error) {
	buff, err := sabre.fillSabreRequestTemplate(&q)
	if err != nil {
		return GDSResult{}, err
	}

	params := map[string]string{
		"mode":   "live",
		"limit":  "50",
		"offset": "1",
	}

	res, err := sabre.requestWithParams("POST", sabreBaseUrl+"/v3.1.0/shop/flights", params, buff)

	if err != nil {
		return GDSResult{}, err
	}
	bs, err := ioutil.ReadAll(res.Body)
	defer res.Body.Close()

	if err != nil {
		return GDSResult{}, err
	}

	js := gjson.ParseBytes(bs)

	return GDSResult{Json: js}, nil
}
