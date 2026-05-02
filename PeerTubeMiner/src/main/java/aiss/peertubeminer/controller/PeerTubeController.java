package aiss.peertubeminer.controller;

import aiss.peertubeminer.model.PTAccountDTO;
import aiss.peertubeminer.model.PTCaptionDTO;
import aiss.peertubeminer.model.PTChannelDTO;
import aiss.peertubeminer.model.PTCommentDTO;
import aiss.peertubeminer.model.PTVideoDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/peertube")
@CrossOrigin(origins = "*")
public class PeerTubeController {

    @GetMapping("/status")
    public String status() {
        return "PeerTubeMiner is running";
    }

    @PostMapping("/videos")
    @ResponseStatus(HttpStatus.CREATED)
    public PTVideoDTO createVideo(@RequestBody PTVideoDTO video) {
        return video;
    }

    @PostMapping("/channels")
    @ResponseStatus(HttpStatus.CREATED)
    public PTChannelDTO createChannel(@RequestBody PTChannelDTO channel) {
        return channel;
    }

    @PostMapping("/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public PTCommentDTO createComment(@RequestBody PTCommentDTO comment) {
        return comment;
    }

    @PostMapping("/captions")
    @ResponseStatus(HttpStatus.CREATED)
    public PTCaptionDTO createCaption(@RequestBody PTCaptionDTO caption) {
        return caption;
    }

    @PostMapping("/accounts")
    @ResponseStatus(HttpStatus.CREATED)
    public PTAccountDTO createAccount(@RequestBody PTAccountDTO account) {
        return account;
    }
}
